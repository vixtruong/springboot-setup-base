package com.example.springbootservice.ultil;

import com.example.springbootservice.core.property.JwtProperties;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTUtils {
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getId())
                .issuer(JwtProperties.STATIC_ISSUER)
                .audience().add(JwtProperties.STATIC_AUDIENCE).and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        try {
            Claims claims = getClaims(token);
            String username = claims.getSubject();

            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }


    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public HashSet<?> extractUserRoles(String token) {
        return extractClaim(token, claims -> {
            return claims.get("roles", HashSet.class);
        });
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(JwtProperties.STATIC_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

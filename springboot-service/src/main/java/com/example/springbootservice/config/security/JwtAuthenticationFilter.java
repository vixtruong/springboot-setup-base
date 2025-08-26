package com.example.springbootservice.config.security;

import com.example.springbootservice.core.enums.ErrorCode;
import com.example.springbootservice.core.exception.AppException;
import com.example.springbootservice.service.RedisService;
import com.example.springbootservice.service.interfaces.IUserService;
import com.example.springbootservice.ultil.JWTUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final IUserService customUserService;
    private final RedisService redisService;

    public JwtAuthenticationFilter(JWTUtils jwtUtils, IUserService customUserService, RedisService redisService) {
        this.jwtUtils = jwtUtils;
        this.customUserService = customUserService;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = jwtUtils.extractJwtFromCookies(request);

            if (StringUtils.hasText(jwt) && jwtUtils.isTokenValid(jwt)) {
                String path = request.getRequestURI();

                if (!path.contains("/auth/refresh") && redisService.isInBlacklist(jwt)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String uid = jwtUtils.extractUserUid(jwt);
                UserDetails userDetails = customUserService.getUserDetailsByUid(uid);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (AppException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid or expired token", e);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Authentication failed", e);
        }

        filterChain.doFilter(request, response);
    }
}

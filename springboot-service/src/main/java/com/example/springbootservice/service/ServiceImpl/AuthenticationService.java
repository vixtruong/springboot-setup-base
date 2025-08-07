package com.example.springbootservice.service.ServiceImpl;

import com.example.springbootservice.core.enums.ErrorCode;
import com.example.springbootservice.core.exception.AppException;
import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
import com.example.springbootservice.entity.RefreshToken;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.RefreshTokenRepository;
import com.example.springbootservice.repository.UserRepository;
import com.example.springbootservice.service.RedisService;
import com.example.springbootservice.ultil.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    JWTUtils jwtUtils;
    RedisService redisService;

    public AuthenticationService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JWTUtils jwtUtils, RedisService redisService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
        this.redisService = redisService;
    }

    public AuthenticationResponse isAuthenticated(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null)
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND,
                    "User with username " + request.getUsername() + " not found");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHORIZED,
                    "Invalid password");

        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(jwtUtils.generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid Authorization header");

        String jwt = authHeader.substring(7);

        String userId = jwtUtils.extractUserId(jwt);

        refreshTokenRepository.deleteAllByUserId(userId);
        redisService.setBacklist(jwt);
    }

    public AuthenticationResponse refreshToken(String refreshToken, HttpServletRequest request) {
        RefreshToken token = validateRefreshToken(refreshToken);
        String jwt = jwtUtils.extractJwtFromRequest(request);

        String userId = jwtUtils.extractUserId(jwt);
        if (!token.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token does not belong to the user");
        }

        User user = token.getUser();

        if (jwtUtils.isTokenValid(jwt)) {
            redisService.setBacklist(jwt);
        }

        String accessToken = jwtUtils.generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public String generateRefreshToken(User user) {
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .isInvoked(false)
                .build();
        refreshTokenRepository.deleteAllByUserId(user.getId());
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken validateRefreshToken(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now()) || token.isInvoked()) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token expired or already used");
        }

        token.setInvoked(true);
        refreshTokenRepository.save(token);
        return token;
    }
}

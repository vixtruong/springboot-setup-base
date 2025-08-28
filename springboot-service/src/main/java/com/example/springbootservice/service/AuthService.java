package com.example.springbootservice.service;

import com.example.springbootservice.core.enums.ErrorCode;
import com.example.springbootservice.core.enums.OAuthProvider;
import com.example.springbootservice.core.enums.Role;
import com.example.springbootservice.core.exception.AppException;
import com.example.springbootservice.dto.request.LocalLoginRequest;
import com.example.springbootservice.dto.request.OAuthLoginRequest;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.response.AuthResponse;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.entity.Account;
import com.example.springbootservice.entity.RefreshToken;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.RefreshTokenRepository;
import com.example.springbootservice.repository.UserRepository;
import com.example.springbootservice.service.interfaces.IAuthService;
import com.example.springbootservice.ultil.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements IAuthService {
    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    JWTUtils jwtUtils;
    RedisService redisService;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JWTUtils jwtUtils, RedisService redisService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
        this.redisService = redisService;
    }

    @Override
    public UserResponse register(UserCreationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = new User(request);

                    HashSet<String> roles = new HashSet<>();
                    roles.add(Role.USER.name());
                    newUser.setRoleSet(roles);

                    Account account = Account.builder()
                            .user(newUser)
                            .provider(OAuthProvider.LOCAL.name())
                            .passwordHash(passwordHash)
                            .build();

                    newUser.addAccount(account);
                    return userRepository.save(newUser);
                });

        boolean hasProvider = user.getAccounts().stream()
                .anyMatch(acc -> acc.getProvider().equals(OAuthProvider.LOCAL.name()));

        if (hasProvider)
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                    "User with email " + request.getEmail() + " already exists");

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoleSet(roles);

        Account newAccount = Account
                .builder()
                .user(user)
                .passwordHash(passwordHash)
                .provider(OAuthProvider.LOCAL.name())
                .build();

        user.addAccount(newAccount);

        return new UserResponse(userRepository.save(user));
    }

    public AuthResponse localLogin(LocalLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with email " + request.getEmail() + " not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        Account account = user.getAccounts().stream()
                .filter(acc -> acc.getProvider().equals(OAuthProvider.LOCAL.name()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "User not registered"));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPasswordHash());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHORIZED,
                    "Invalid password");

        return AuthResponse.builder()
                .accessToken(jwtUtils.generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    public AuthResponse oAuthLogin(OAuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = new User(request.getEmail(), request.getFullName(), request.getAvatarUrl());

                    HashSet<String> roles = new HashSet<>();
                    roles.add(Role.USER.name());
                    newUser.setRoleSet(roles);

                    Account account = Account.builder()
                            .user(newUser)
                            .provider(request.getProvider().toUpperCase())
                            .build();

                    newUser.addAccount(account);
                    return userRepository.save(newUser);
                });

        boolean hasProvider = user.getAccounts().stream()
                .anyMatch(acc -> acc.getProvider().equals(request.getProvider().toUpperCase()));

        if (!hasProvider) {
            Account account = Account.builder()
                    .user(user)
                    .provider(request.getProvider().toUpperCase())
                    .build();
            user.addAccount(account);
            userRepository.save(user);
        }

        if (user.getAvatarUrl() == null && request.getAvatarUrl() != null ||
                !Objects.equals(user.getAvatarUrl(), request.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        return AuthResponse.builder()
                .accessToken(jwtUtils.generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    public void logout(HttpServletRequest request) {
        String jwt = jwtUtils.extractJwtFromCookies(request);
        if (jwt == null) {
            return;
        }

        String uid = jwtUtils.extractUserUid(jwt);
        refreshTokenRepository.deleteAllByUserUid(uid);
        redisService.setBacklist(jwt);
    }

    public AuthResponse refreshToken(String refreshToken, HttpServletRequest request) {
        RefreshToken token = validateRefreshToken(refreshToken);
        String jwt = jwtUtils.extractJwtFromCookies(request);
        if (jwt != null) {
            String uid = jwtUtils.extractUserUid(jwt);
            if (!token.getUser().getUid().equals(uid)) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token does not belong to the user");
            }

            if (jwtUtils.isTokenValid(jwt)) {
                redisService.setBacklist(jwt);
            }
        }

        User user = token.getUser();
        String accessToken = jwtUtils.generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);

        return AuthResponse.builder()
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
        refreshTokenRepository.deleteAllByUserUid(user.getUid());
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken validateRefreshToken(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token expired or already used");
        }

        refreshTokenRepository.save(token);
        return token;
    }
}

package com.example.springbootservice.service.ServiceImpl;

import com.example.springbootservice.core.enums.ErrorCode;
import com.example.springbootservice.core.exception.AppException;
import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.UserRepository;
import com.example.springbootservice.service.RedisService;
import com.example.springbootservice.ultil.JWTUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    JWTUtils jwtUtils;
    RedisService redisService;

    public AuthenticationService(UserRepository userRepository, JWTUtils jwtUtils, RedisService redisService) {
        this.userRepository = userRepository;
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
                .build();
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid Authorization header");

        String jwt = authHeader.substring(7);
        redisService.setBacklist(jwt);
    }
}

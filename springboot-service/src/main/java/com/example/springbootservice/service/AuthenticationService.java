package com.example.springbootservice.service;

import com.example.springbootservice.core.AppConstants;
import com.example.springbootservice.core.AppException;
import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.UserRepository;
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

    public AuthenticationService(UserRepository userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public AuthenticationResponse isAuthenticated(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null)
            throw new AppException(AppConstants.ExceptionType.UNAUTHORIZED,
                    "User with username " + request.getUsername() + " not found");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(AppConstants.ExceptionType.UNAUTHORIZED,
                    "Invalid password");

        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(jwtUtils.generateAccessToken(user.getUsername()))
                .build();
    }
}

package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.request.RefreshTokenRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
import com.example.springbootservice.dto.response.MessageResponse;
import com.example.springbootservice.service.RedisService;
import com.example.springbootservice.service.ServiceImpl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    RedisService redisService;

    public AuthenticationController(AuthenticationService authenticationService, RedisService redisService) {
        this.authenticationService = authenticationService;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    OkResponse<?> login(@RequestBody @Valid LoginRequest request) {
        AuthenticationResponse authResponse = authenticationService.isAuthenticated(request);

        return OkResponse.builder()
                .data(authResponse)
                .build();
    }

    @PostMapping("/logout")
    OkResponse<?> logout(@RequestHeader("Authorization") String authHeader) {
        authenticationService.logout(authHeader);

        return OkResponse.builder()
                .data(new MessageResponse("Logged out successfully"))
                .build();
    }

    @PostMapping("/refresh")
    OkResponse<?> refreshToken(@RequestBody @Valid RefreshTokenRequest tokenRequest,
                               HttpServletRequest request) {
        AuthenticationResponse response =
                authenticationService.refreshToken(tokenRequest.getRefreshToken(), request);

        return OkResponse.builder()
                .data(response)
                .build();
    }
}

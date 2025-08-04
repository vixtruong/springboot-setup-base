package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
import com.example.springbootservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    OkResponse login(@RequestBody @Valid LoginRequest request) {
        AuthenticationResponse authResponse = authenticationService.isAuthenticated(request);

        return OkResponse.builder()
                .data(authResponse)
                .build();
    }
}

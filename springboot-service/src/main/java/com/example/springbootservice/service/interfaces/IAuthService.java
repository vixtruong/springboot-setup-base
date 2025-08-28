package com.example.springbootservice.service.interfaces;

import com.example.springbootservice.dto.request.LocalLoginRequest;
import com.example.springbootservice.dto.request.OAuthLoginRequest;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.response.AuthResponse;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.entity.RefreshToken;
import com.example.springbootservice.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    UserResponse register(UserCreationRequest request);

    AuthResponse localLogin(LocalLoginRequest request);

    AuthResponse oAuthLogin(OAuthLoginRequest request);

    void logout(HttpServletRequest request);

    AuthResponse refreshToken(String refreshToken, HttpServletRequest request);

    String generateRefreshToken(User user);

    RefreshToken validateRefreshToken(String tokenStr);
}

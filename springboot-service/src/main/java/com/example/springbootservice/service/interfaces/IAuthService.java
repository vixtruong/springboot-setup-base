package com.example.springbootservice.service.interfaces;

import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
import com.example.springbootservice.entity.RefreshToken;
import com.example.springbootservice.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    AuthenticationResponse isAuthenticated(LoginRequest request);

    void logout(HttpServletRequest request);

    AuthenticationResponse refreshToken(String refreshToken, HttpServletRequest request);

    String generateRefreshToken(User user);

    RefreshToken validateRefreshToken(String tokenStr);
}

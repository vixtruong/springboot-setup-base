package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.LocalLoginRequest;
import com.example.springbootservice.dto.request.OAuthLoginRequest;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.response.AuthResponse;
import com.example.springbootservice.dto.response.MessageResponse;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.service.interfaces.IAuthService;
import com.example.springbootservice.service.interfaces.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    IAuthService authService;

    public AuthController(IAuthService authService, IUserService userService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    OkResponse login(@RequestBody @Valid LocalLoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.localLogin(request);

        Cookie accessCookie = generateAccessTokenCookie(authResponse.getAccessToken());
        response.addCookie(accessCookie);

        Cookie refreshCookie = generateRefreshTokenCookie(authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return new OkResponse();
    }

    @PostMapping("/oauth")
    OkResponse oAuthLogin(@RequestBody @Valid OAuthLoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.oAuthLogin(request);

        Cookie accessCookie = generateAccessTokenCookie(authResponse.getAccessToken());
        response.addCookie(accessCookie);

        Cookie refreshCookie = generateRefreshTokenCookie(authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return new OkResponse();
    }

    @PostMapping("/register")
    OkResponse register(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = authService.register(request);
        return new OkResponse(user);
    }

    @PostMapping("/logout")
    OkResponse logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request);

        // clear cookie
        response.addCookie(clearCookie("refreshToken"));
        response.addCookie(clearCookie("accessToken"));

        return new OkResponse(new MessageResponse("Logged out successfully"));
    }

    @PostMapping("/refresh")
    OkResponse refreshToken(@CookieValue("refreshToken") String refreshToken,
                            HttpServletRequest request, HttpServletResponse response) {
        AuthResponse authResponse =
                authService.refreshToken(refreshToken, request);

        Cookie accessCookie = generateAccessTokenCookie(authResponse.getAccessToken());
        response.addCookie(accessCookie);

        Cookie refreshCookie = generateRefreshTokenCookie(authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return new OkResponse(response);
    }

    private Cookie generateAccessTokenCookie(String accessToken) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }

    private Cookie generateRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }

    private Cookie clearCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}

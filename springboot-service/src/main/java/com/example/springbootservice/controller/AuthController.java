package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.LoginRequest;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.response.AuthenticationResponse;
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
    IAuthService authenticationService;
    IUserService userService;

    public AuthController(IAuthService authenticationService, IUserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    OkResponse login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
//        AuthenticationResponse authResponse = authenticationService.isAuthenticated(request);

//        Cookie accessCookie = generateAccessTokenCookie(authResponse.getAccessToken());
//        response.addCookie(accessCookie);
//
//        Cookie refreshCookie = generateRefreshTokenCookie(authResponse.getRefreshToken());
//        response.addCookie(refreshCookie);

        return new OkResponse();
    }

    @PostMapping("/register")
    OkResponse register(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);
        return new OkResponse(user);
    }

    @PostMapping("/logout")
    OkResponse logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request);

        // clear cookie
        response.addCookie(clearCookie("refreshToken"));
        response.addCookie(clearCookie("accessToken"));

        return new OkResponse(new MessageResponse("Logged out successfully"));
    }

    @PostMapping("/refresh")
    OkResponse refreshToken(@CookieValue("refreshToken") String refreshToken,
                            HttpServletRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse =
                authenticationService.refreshToken(refreshToken, request);

        Cookie accessCookie = generateAccessTokenCookie(authResponse.getAccessToken());
        response.addCookie(accessCookie);

        Cookie refreshCookie = generateRefreshTokenCookie(authResponse.getRefreshToken());
        response.addCookie(refreshCookie);

        return new OkResponse(response);
    }

    private Cookie generateRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        return cookie;
    }

    private Cookie generateAccessTokenCookie(String accessToken) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
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

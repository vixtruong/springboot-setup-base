package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    OkResponse getUsers() {
        List<UserResponse> users = userService.getUsers();
        return new OkResponse(users);
    }

    @GetMapping("/profile")
    OkResponse getProfile(@CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null)
            return new OkResponse();

        UserResponse user = userService.getProfile(accessToken);

        return new OkResponse(user);
    }

    @PutMapping("/{userId}")
    OkResponse updateUser(@PathVariable("userId") String userId,
                          @RequestBody @Valid UserUpdateRequest request) {
        UserResponse user = userService.updateUser(userId, request);
        return new OkResponse(user);
    }

    @GetMapping("/{userId}")
    OkResponse getUserById(@PathVariable("userId") String userId) {
        UserResponse user = userService.getUserById(userId);
        return new OkResponse(user);
    }
}

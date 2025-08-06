package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    OkResponse<?> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return OkResponse.builder()
                .data(users)
                .build();
    }

    @PostMapping
    OkResponse<?> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse user = userService.createUser(request);
        return OkResponse.builder()
                .data(user)
                .build();
    }

    @PutMapping("/{userId}")
    OkResponse<?> updateUser(@PathVariable("userId") String userId,
                          @RequestBody @Valid UserUpdateRequest request) {
        UserResponse user = userService.updateUser(userId, request);
        return OkResponse.builder()
                .data(user)
                .build();
    }

    @GetMapping("/{userId}")
    OkResponse<?> getUserById(@PathVariable("userId") String userId) {
        UserResponse user = userService.getUserById(userId);
        return OkResponse.builder()
                .data(user)
                .build();
    }
}

package com.example.springbootservice.controller;

import com.example.springbootservice.core.response.OkResponse;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    OkResponse getUsers() {
        List<User> users = userService.getUsers();
        return new OkResponse(users);
    }

    @PostMapping
    OkResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        User user = userService.createRequest(request);
        return new OkResponse(user);
    }


    @GetMapping("/{userId}")
    OkResponse<User> getUserById(@PathVariable("userId") String userId) {
        User user = userService.getUserById(userId);
        return new OkResponse(user);
    }
}

package com.example.springbootservice.controller;

import com.example.springbootservice.core.AppResponse;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    ResponseEntity<AppResponse<User>> createUser(@RequestBody @Valid UserCreationRequest request) {
        User user = userService.createRequest(request);
        return ResponseEntity.ok(AppResponse.success("Create user successfully.", user));
    }


    @GetMapping("/{userId}")
    ResponseEntity<AppResponse<User>> getUserById(@PathVariable("userId") String userId) {
        User user = userService.getUserById(userId);

        return ResponseEntity.ok(AppResponse.success("", user));
    }
}

package com.example.springbootservice.service;

import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    List<UserResponse> getUsers();
    UserResponse getUserById(String userId);
    UserDetails getUserDetailsById(String userId);
}
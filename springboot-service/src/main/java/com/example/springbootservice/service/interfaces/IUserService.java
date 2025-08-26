package com.example.springbootservice.service.interfaces;

import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    List<UserResponse> getUsers();

    UserResponse getProfile(HttpServletRequest request);

    UserResponse getUserById(String userId);

    UserResponse getUserByUid(String uid);

    UserDetails getUserDetailsByUid(String uid);
}
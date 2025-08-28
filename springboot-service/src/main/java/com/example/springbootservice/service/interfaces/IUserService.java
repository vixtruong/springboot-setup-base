package com.example.springbootservice.service.interfaces;

import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    UserResponse updateUser(String userId, UserUpdateRequest request);

    List<UserResponse> getUsers();

    UserResponse getProfile(String accessToken);

    UserResponse getUserById(String userId);

    UserResponse getUserByUid(String uid);

    UserDetails getUserDetailsByUid(String uid);

    void uploadAvatar(String accessToken, MultipartFile file);
}
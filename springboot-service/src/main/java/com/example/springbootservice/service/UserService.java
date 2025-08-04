package com.example.springbootservice.service;

import com.example.springbootservice.core.AppConstants;
import com.example.springbootservice.core.AppException;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.mapper.UserMapper;
import com.example.springbootservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(AppConstants.ExceptionType.DUPLICATE_RESOURCE,
                    "User with username " + request.getUsername() + " already exists");

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        AppConstants.ExceptionType.ENTITY_NOT_FOUND,
                        "User with ID " + userId + " not found"
                ));

        if (!isUserChanged(user, request))
            throw new AppException(AppConstants.ExceptionType.BAD_REQUEST,
                    "No information has been changed for update");

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(String userId) {
        return userRepository.findById(userId).map(userMapper::toUserResponse)
                .orElseThrow(() -> new AppException(AppConstants.ExceptionType.ENTITY_NOT_FOUND, "User with ID " + userId + " not found"));
    }

    private boolean isUserChanged(User user, UserUpdateRequest request) {
        return !Objects.equals(user.getFullName(), request.getFullName())
                || !Objects.equals(user.getBirthday(), request.getBirthday());
    }
}

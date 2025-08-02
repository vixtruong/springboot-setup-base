package com.example.springbootservice.service;

import com.example.springbootservice.core.AppConstants;
import com.example.springbootservice.core.AppException;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createRequest(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(AppConstants.ExceptionType.DUPLICATE_RESOURCE,
                    "User with username " + request.getUsername() + " already exists");

        User user = new User(request);

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(AppConstants.ExceptionType.ENTITY_NOT_FOUND, "User with ID " + userId + " not found"));
    }
}

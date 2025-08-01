package com.example.springbootservice.service;

import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createRequest(UserCreationRequest request) {
        User existUser = userRepository.findUserByUsername(request.getUsername());

        if (existUser != null)
            throw new RuntimeException("User with username " + existUser.getUsername() + " already exists");

        User user = new User(request);

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));
    }
}

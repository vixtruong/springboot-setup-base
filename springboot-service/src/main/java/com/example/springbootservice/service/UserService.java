package com.example.springbootservice.service;

import com.example.springbootservice.core.enums.ErrorCode;
import com.example.springbootservice.core.enums.Role;
import com.example.springbootservice.core.exception.AppException;
import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.dto.request.UserUpdateRequest;
import com.example.springbootservice.dto.response.UserResponse;
import com.example.springbootservice.entity.Account;
import com.example.springbootservice.entity.User;
import com.example.springbootservice.repository.UserRepository;
import com.example.springbootservice.config.security.CustomUserDetails;
import com.example.springbootservice.service.interfaces.IUserService;
import com.example.springbootservice.ultil.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;
    private final JWTUtils jWTUtils;

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.ENTITY_NOT_FOUND,
                        "User with ID " + userId + " not found"
                ));

        if (!isUserChanged(user, request))
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "No information has been changed for update");

        user.setFullName(request.getFullName());
        user.setBirthday(request.getBirthday());

        return new UserResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse::new)
                .toList();
    }

    @Override
    public UserResponse getProfile(String accessToken) {
        String userUid = jWTUtils.extractUserUid(accessToken);

        return getUserByUid(userUid);
    }

    @Override
    public UserResponse getUserById(String userId) {
        return userRepository.findById(userId).map(UserResponse::new)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with ID " + userId + " not found"));
    }

    @Override
    public UserResponse getUserByUid(String uid) {
        return userRepository.findByUid(uid).map(UserResponse::new)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with Uid " + uid + " not found"));
    }

    @Override
    public UserDetails getUserDetailsByUid(String uid) {
        return userRepository.findByUid(uid).map(CustomUserDetails::new)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with Uid " + uid + " not found"));
    }

    private boolean isUserChanged(User user, UserUpdateRequest request) {
        return !Objects.equals(user.getFullName(), request.getFullName())
                || !Objects.equals(user.getBirthday(), request.getBirthday());
    }
}

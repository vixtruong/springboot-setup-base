package com.example.springbootservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.springbootservice.config.CloudinaryConfig;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    UserRepository userRepository;
    JWTUtils jWTUtils;
    Cloudinary cloudinary;

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

    @Override
    public void uploadAvatar(String accessToken, MultipartFile file) {
        String userUid = jWTUtils.extractUserUid(accessToken);

        User user = userRepository.findByUid(userUid)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with Uid " + userUid + " not found"));

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "spring-auth-project/users/" + user.getId(),
                            "public_id", "avatar",
                            "overwrite", true));

            String avatarUrl = (String) uploadResult.get("secure_url");
            user.setAvatarUrl(avatarUrl);

            userRepository.save(user);
        } catch (IOException e) {
            throw new AppException(ErrorCode.IO_ERROR, "Failed to upload avatar: " + e.getMessage());
        }
    }


    private boolean isUserChanged(User user, UserUpdateRequest request) {
        return !Objects.equals(user.getFullName(), request.getFullName())
                || !Objects.equals(user.getBirthday(), request.getBirthday());
    }
}

package com.example.springbootservice.dto.response;

import com.example.springbootservice.entity.User;
import com.example.springbootservice.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends BaseEntity {
    String email;
    String fullName;
    String avatarUrl;
    LocalDate birthday;
    Set<String> roles;

    public UserResponse(User user) {
        setId(user.getId());
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.birthday = user.getBirthday();
        this.roles = user.getRoleSet();
        this.avatarUrl = user.getAvatarUrl();
        setCreatedTime(user.getCreatedTime());
        setUpdatedTime(user.getUpdatedTime());
    }
}

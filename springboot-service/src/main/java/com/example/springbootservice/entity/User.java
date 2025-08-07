package com.example.springbootservice.entity;

import com.example.springbootservice.dto.request.UserCreationRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;
    private String fullName;
    private LocalDate birthday;
    @Column(columnDefinition = "TEXT")
    private String roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RefreshToken> refreshTokens;

    public User(UserCreationRequest request) {
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.fullName = request.getFullName();
        this.birthday = request.getBirthday();
    }

    @Transient
    public Set<String> getRoleSet() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(roles, new TypeReference<Set<String>>() {
            });
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    public void setRoleSet(Set<String> roleSet) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.roles = mapper.writeValueAsString(roleSet);
        } catch (Exception e) {
            this.roles = "[]";
        }
    }
}

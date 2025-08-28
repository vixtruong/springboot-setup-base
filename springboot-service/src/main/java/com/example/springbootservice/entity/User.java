package com.example.springbootservice.entity;

import com.example.springbootservice.dto.request.UserCreationRequest;
import com.example.springbootservice.entity.base.BaseEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    private String uid;
    private String email;
    private String fullName;
    private String avatarUrl;
    private LocalDate birthday;
    private boolean isActive;
    @Column(columnDefinition = "TEXT")
    private String roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Account> accounts;

    public User(UserCreationRequest request) {
        this.email = request.getEmail();
        this.fullName = request.getFullName();
        this.birthday = request.getBirthday();
        this.isActive = true;
        this.uid = UUID.randomUUID().toString();
    }

    public User(String email, String fullName, String avatarUrl) {
        this.email = email;
        this.fullName = fullName;
        this.uid = UUID.randomUUID().toString();
        this.avatarUrl = avatarUrl;
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

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new HashSet<>();
        }
        accounts.add(account);
        account.setUser(this);
    }
}

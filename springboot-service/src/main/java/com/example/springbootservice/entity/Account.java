package com.example.springbootservice.entity;

import com.example.springbootservice.entity.base.BaseEntityNoUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Accounts")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntityNoUpdate {
    @ManyToOne
    @JoinColumn(name = "UserId")
    @EqualsAndHashCode.Exclude
    private User user;
    private String provider;
    private String passwordHash;
}

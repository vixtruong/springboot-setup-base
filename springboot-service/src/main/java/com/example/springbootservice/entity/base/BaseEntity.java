package com.example.springbootservice.entity.base;

import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "CreatedTime")
    private Long createdTime;

    @Column(name = "UpdatedTime")
    private Long updatedTime;

    @PrePersist
    protected void onCreate() {
        this.createdTime = System.currentTimeMillis();
        this.updatedTime = System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = System.currentTimeMillis();
    }
}

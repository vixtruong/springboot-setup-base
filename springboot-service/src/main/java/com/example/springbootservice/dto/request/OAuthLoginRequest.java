package com.example.springbootservice.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthLoginRequest {
    private String email;
    private String fullName;
    private String provider;
}

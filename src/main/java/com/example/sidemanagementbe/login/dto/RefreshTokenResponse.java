package com.example.sidemanagementbe.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshTokenResponse {
    private String id;
    private String refreshToken;
}

package com.example.sidemanagementbe.login.dto;

import lombok.Getter;

@Getter
public class AccessTokenRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }
}
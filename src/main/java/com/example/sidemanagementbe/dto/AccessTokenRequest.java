package com.example.sidemanagementbe.dto;

import lombok.Getter;

@Getter
public class AccessTokenRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }
}
package com.example.sidemanagementbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class RefreshTokenResponse {
    private String id;
    private String refreshToken;
}

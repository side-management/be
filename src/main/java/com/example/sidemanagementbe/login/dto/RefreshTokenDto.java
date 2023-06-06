package com.example.sidemanagementbe.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class RefreshTokenDto {
    private String refreshToken;
    private Long memberId;
}

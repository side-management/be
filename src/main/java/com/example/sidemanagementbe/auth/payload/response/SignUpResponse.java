package com.example.sidemanagementbe.auth.payload.response;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SignUpResponse {
    private final String accessToken;
    private final String refreshToken;
}

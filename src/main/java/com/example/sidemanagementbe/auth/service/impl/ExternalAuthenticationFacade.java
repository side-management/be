package com.example.sidemanagementbe.auth.service.impl;

import com.example.sidemanagementbe.auth.infrastructure.authentication.KakaoApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalAuthenticationFacade {

    private final KakaoApiClient kakaoApiClient;

    public String execute(String accessToken) {
        var response = kakaoApiClient.getUserProfile(accessToken);
        return response.getId();
    }
}

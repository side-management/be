package com.example.sidemanagementbe.auth.infrastructure.authentication;

import com.example.sidemanagementbe.auth.payload.dto.KaKaoProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "kakaoApiClient",
        url = "https://kapi.kakao.com"
)
public interface KakaoApiClient {
    @GetMapping("/v2/user/me")
    KaKaoProfileResponse getUserProfile(@RequestHeader("Authorization") final String accessToken);

}

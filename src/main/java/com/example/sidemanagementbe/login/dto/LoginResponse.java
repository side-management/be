package com.example.sidemanagementbe.login.dto;

import com.example.sidemanagementbe.login.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    @Schema(description = "사용자 ID", nullable = false, example = "2817797040")
    private Long id;
    @Schema(description = "사용자 닉네임", nullable = false, example = "홍길동")
    private String nickname;
    @Schema(description = "사용자 email", nullable = false, example = "example@naver.com")
    private String email;
    @Schema(description = "사용자 카카오 프로필 사진 cdn 링크", nullable = false, example = "CDN 주소")
    private String imageUrl;
    @Schema(description = "사용자 역할", nullable = false, example = "USER")
    private MemberRole role;
    @Schema(description = "토큰 타입", nullable = false, example = "Bearer")
    private String tokenType;
    @Schema(description = "access token", nullable = false, example = "JWT Access Token")
    private String accessToken;
    @Schema(description = "refresh token", nullable = false, example = "JWT Refresh Token")
    private String refreshToken;

    @Builder
    public LoginResponse(Long id, String nickname, String email, String imageUrl, MemberRole role, String tokenType,
                         String accessToken, String refreshToken) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

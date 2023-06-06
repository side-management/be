package com.example.sidemanagementbe.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponse {
    @Schema(description = "재발급된 access token", nullable = false, example = "JWT token")
    private String accessToken;


}

package com.example.sidemanagementbe.login.controller;

import com.example.sidemanagementbe.login.dto.AccessTokenRequest;
import com.example.sidemanagementbe.login.dto.AccessTokenResponse;
import com.example.sidemanagementbe.login.dto.LoginResponse;
import com.example.sidemanagementbe.login.service.OauthService;
import com.example.sidemanagementbe.login.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "로그인", description = "로그인 관련 api")
public class LoginController {
    private final OauthService oauthService;
    private final TokenService tokenService;


    @Operation(summary = "카카오 로그인 시 호출 API", description = "https://kauth.kakao.com/oauth/authorize를 통해 provider와 code를 넘겨주면 JWT 토큰들과 로그인 정보를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - code 재사용 불가")
    })
    @GetMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        LoginResponse loginResponse = oauthService.login(provider, code);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "access token 재발급 API", description = "Redis Cache에 refresh token 조회 후 유효한 경우 access token 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "access token 재발급 성공", content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - 재발급 실패")
    })
    @Secured("USER")
    @PostMapping("/access-token")
    public ResponseEntity<AccessTokenResponse> regenerateAccessToken(@RequestBody final AccessTokenRequest request) {
        AccessTokenResponse accessTokenResponse = tokenService.regenerateAccessToken(request);
        return ResponseEntity.ok(accessTokenResponse);
    }

}

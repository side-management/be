package com.example.sidemanagementbe.controller;

import com.example.sidemanagementbe.dto.AccessTokenRequest;
import com.example.sidemanagementbe.dto.AccessTokenResponse;
import com.example.sidemanagementbe.dto.LoginResponse;

import com.example.sidemanagementbe.service.OauthService;
import com.example.sidemanagementbe.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "로그인", description = "로그인 관련 api")
public class LoginController {
    private final OauthService oauthService;
    private final TokenService tokenService;


    @GetMapping("/login/oauth/{provider}")
    @Operation(summary = "카카오 로그인 시 호출 API", description = "https://kauth.kakao.com/oauth/authorize를 통해 provider와 code를 넘겨주면 JWT 토큰들과 로그인 정보를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
          //  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code){
        LoginResponse loginResponse = oauthService.login(provider, code);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/access-token")
    public ResponseEntity<AccessTokenResponse> generateNewAccessToken(@RequestBody final AccessTokenRequest request) {
        AccessTokenResponse accessTokenResponse = tokenService.generateAccessToken(request);
        return ResponseEntity.ok(accessTokenResponse);
    }
}

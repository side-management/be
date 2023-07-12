package com.example.sidemanagementbe.auth.service.impl;


import com.example.sidemanagementbe.auth.entity.AuthenticationToken;
import com.example.sidemanagementbe.auth.infrastructure.redis.RedisAuthenticationTokenRepository;
import com.example.sidemanagementbe.web.jwt.util.JwtProvider;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssuanceTokenService {
    private final JwtProvider tokenProvider;
    private final RedisAuthenticationTokenRepository authenticationTokenRepository;

    public IssuanceTokenDto execute(Long id) {
        var now = Instant.now();

        var accessToken = tokenProvider.createAccessToken(id, now);
        var refreshToken = tokenProvider.createRefreshToken(id, now);

        authenticationTokenRepository.save(AuthenticationToken.builder()
                .memberId(id)
                .refreshToken(refreshToken)
                .build()
        );

        return IssuanceTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

@Getter
class IssuanceTokenDto {
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private IssuanceTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
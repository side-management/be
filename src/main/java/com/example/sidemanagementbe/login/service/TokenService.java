package com.example.sidemanagementbe.login.service;

import com.example.sidemanagementbe.login.dto.AccessTokenRequest;
import com.example.sidemanagementbe.login.dto.AccessTokenResponse;
import com.example.sidemanagementbe.login.exception.InvalidRefreshTokenException;
import com.example.sidemanagementbe.login.repository.RefreshTokenRepository;
import com.example.sidemanagementbe.web.security.util.JwtTokenProvider;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, String> redisTemplate;

    public AccessTokenResponse regenerateAccessToken(final AccessTokenRequest request) {

        //refresh token이 redis 캐시에 존재하는지 검증
        refreshTokenRepository.findById(request.getRefreshToken())
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        //redis 캐시에 기존 accessToken 삭제

        if (redisTemplate.hasKey(request.getAccessToken())) {
            redisTemplate.delete(request.getAccessToken());
        }

        Map<String, Object> claims = jwtTokenProvider.getPayload(request.getAccessToken());

        //기존 accessToken의 random UUID만 변경해서 다시 생성
        claims.put("random UUID", UUID.randomUUID().toString());
        String accessToken = jwtTokenProvider.createAccessToken(claims);

        //redis cache에 새로운 accessToken 저장
        redisTemplate.opsForValue().set(accessToken, "valid", jwtTokenProvider.getAccessTokenValidityInMilliseconds(),
                TimeUnit.MILLISECONDS);

        return new AccessTokenResponse(accessToken);
    }
}
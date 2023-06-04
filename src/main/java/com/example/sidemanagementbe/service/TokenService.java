package com.example.sidemanagementbe.service;

import com.example.sidemanagementbe.dto.AccessTokenRequest;
import com.example.sidemanagementbe.dto.AccessTokenResponse;
import com.example.sidemanagementbe.entity.RefreshToken;
import com.example.sidemanagementbe.exception.InvalidRefreshTokenException;
import com.example.sidemanagementbe.repository.MemberRepository;
import com.example.sidemanagementbe.repository.RefreshTokenRepository;
import com.example.sidemanagementbe.security.util.JwtTokenProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void saveRefreshToken(Long memberId, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }

    public String getRefreshToken(String tokenId) {
        RefreshToken token = refreshTokenRepository.findById(tokenId).orElse(null);
        return (token != null) ? token.getRefreshToken() : null;
    }

    public void deleteByRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

    public AccessTokenResponse generateAccessToken(final AccessTokenRequest request) {

        Optional<RefreshToken> refreshToken1 = refreshTokenRepository.findById(request.getRefreshToken());
        RefreshToken refreshToken3 = refreshToken1.get();
        log.info("regreshToken 값:"+refreshToken3.getRefreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findById(request.getRefreshToken())
                .orElseThrow(InvalidRefreshTokenException::new);
        String token = refreshToken.getRefreshToken();


        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtTokenProvider.getAccessTokenValidityInMilliseconds());

        String accessToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtTokenProvider.getSecretKey())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(token)
                .compact();

        return new AccessTokenResponse(accessToken);
    }
}
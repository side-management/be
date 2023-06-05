package com.example.sidemanagementbe.login.service;

import com.example.sidemanagementbe.login.dto.AccessTokenRequest;
import com.example.sidemanagementbe.login.dto.AccessTokenResponse;
import com.example.sidemanagementbe.login.entity.RefreshToken;
import com.example.sidemanagementbe.login.exception.InvalidRefreshTokenException;
import com.example.sidemanagementbe.login.repository.RefreshTokenRepository;
import com.example.sidemanagementbe.login.security.util.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        log.info("regreshToken 값:" + refreshToken3.getRefreshToken());
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
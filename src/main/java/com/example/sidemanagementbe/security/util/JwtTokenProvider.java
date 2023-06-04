package com.example.sidemanagementbe.security.util;

import com.example.sidemanagementbe.dto.AccessTokenRequest;
import com.example.sidemanagementbe.entity.Member;
import com.example.sidemanagementbe.entity.RefreshToken;
import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Component
@Getter
@Slf4j
public class JwtTokenProvider {

    private long accessTokenValidityInMilliseconds;

    private long refreshTokenValidityInMilliseconds;

    private Map<String, String> keyValues;

    @Value("C:/secret-key/jwt-secret-key.txt")
    private String secretKeyFile;

    private String secretKey;

    public String createAccessToken(String payload) {
        log.info("accessTokenValidityInMilliseconds 값:"+accessTokenValidityInMilliseconds);
        log.info("payload 값:"+payload);
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return createToken(generatedString, refreshTokenValidityInMilliseconds);
    }




    public String createToken(String payload, long expirationTimeInMillis) {
        return Jwts.builder()
                .setSubject(payload)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    @PostConstruct
    private void init() {
        keyValues = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(secretKeyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    keyValues.put(key, value);
                }
            }

            secretKey = keyValues.get("secretKey");
            accessTokenValidityInMilliseconds = Long.parseLong(keyValues.get("accessTokenValidityInMilliseconds"));
            refreshTokenValidityInMilliseconds = Long.parseLong(keyValues.get("accessTokenValidityInMilliseconds"));




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPayload(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e){
            throw new RuntimeException("유효하지 않은 토큰 입니다");
        }
    }

    public Long validateAccessToken(String accessToken) {
        String secretKey = getSecretKey();
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken);

            // 토큰의 만료 여부 검사
            Date expirationDate = claimsJws.getBody().getExpiration();
            if (Instant.now().isAfter(expirationDate.toInstant())) {
                throw new BadCredentialsException("Access token has expired");
            }

            // 토큰의 변조 여부 검사
            String originalSignature = claimsJws.getSignature();
            String calculatedSignature = Jwts.builder()
                    .setPayload(claimsJws.getBody().toString())
                    .signWith(SignatureAlgorithm.HS256, getSecretKey())
                    .compact();
            if (!originalSignature.equals(calculatedSignature)) {
                throw new BadCredentialsException("Invalid access token");
            }

            // 예시: 토큰에서 memberId 추출하기
            String memberId = claimsJws.getBody().get("memberId", String.class);
            return Long.parseLong(memberId);

        } catch (Exception e) {
            // 토큰 검증 실패 시 예외 처리합니다.
            throw new BadCredentialsException("Invalid access token");
        }
    }
}

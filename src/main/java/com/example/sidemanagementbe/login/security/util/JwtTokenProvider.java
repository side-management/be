package com.example.sidemanagementbe.login.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

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

    public String createAccessToken(Map<String, Object> claims) {
        log.info("accessTokenValidityInMilliseconds 값:" + accessTokenValidityInMilliseconds);
        return createToken(claims, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        Map<String, Object> claims = new HashMap<>();

        String generatedString = new String(array, StandardCharsets.UTF_8);
        claims.put("random byte", generatedString);
        return createToken(claims, refreshTokenValidityInMilliseconds);
    }


    public String createToken(Map<String, Object> claims, long expirationTimeInMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

    public Claims decodeJwtPayload(String jwtToken) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return jws.getBody();
    }

    public String getMemberId(String token) {
        Map<String, Object> payloadMap = getPayload(token);
        return (String) payloadMap.get("id");
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

    public Claims getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new RuntimeException("유효하지 않은 토큰 입니다");
        }
    }

    public boolean validateToken(String token) {
        String secretKey = getSecretKey();

        // 토큰의 변조 여부 검사
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        // 토큰의 만료 여부 검사
        Date expirationDate = claimsJws.getBody().getExpiration();
        if (Instant.now().isAfter(expirationDate.toInstant())) {
            throw new BadCredentialsException("Access token has expired");
        }

        return true;
    }

}

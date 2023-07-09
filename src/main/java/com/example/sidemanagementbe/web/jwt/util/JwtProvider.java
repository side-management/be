package com.example.sidemanagementbe.web.jwt.util;


import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
    private static final String issuer = "TEAM-SAIDA-BE"; // 발급자 명칭 변경 예정
    private static final Long accessTokenExpiry = 7 * 24 * 60 * 60L; // 1주
    private static final Long refreshTokenExpiry = 14 * 24 * 60 * 60L; // 2주
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String createAccessToken(final Long id, Instant issuanceTime) {
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(issuanceTime)
                .expiresAt(issuanceTime.plusSeconds(accessTokenExpiry))
                .subject(String.valueOf(id))
                .claim("id", id)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String createRefreshToken(final Long id, Instant issuanceTime) {
        // UUID 혹은 id를 사용할지 고민중
        // UUID.fromString(LocalDateTime.now().toString()).toString();

        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(issuanceTime)
                .expiresAt(issuanceTime.plusSeconds(refreshTokenExpiry))
                .subject(String.valueOf(id))
                .claim("id", id)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Map<String, Object> decode(String token) {
        return jwtDecoder.decode(token).getClaims();
    }


    public Long extractToValueFrom(String token) {
        try {
            return Long.parseLong(this.decode(token).get("id").toString());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("유효하지 않은 토큰 입니다.");
        }
    }

    public boolean validateToken(final String token) {
        try {
            this.decode(token);
            return true;
        } catch (BadJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }

}
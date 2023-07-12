package com.example.sidemanagementbe.web.jwt.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class JwtProviderTest {

    private JwtProvider tokenProvider;

    @BeforeEach
    void setup() throws JOSEException {
        tokenProvider = FakeTokenProviderGenerator.createTokenProvider();
    }


    @Nested
    @DisplayName("토큰을 발급하는 상황에서")
    class TokenIssuance {
        @ParameterizedTest
        @ValueSource(longs = {1, 2, 3, Long.MAX_VALUE})
        void access_token_이_정상적으로_발급된다(final long id) throws JOSEException {
            var token = tokenProvider.createAccessToken(id, Instant.now());

            assertAll(() -> assertThat(token).isNotNull(),
                    () -> assertThat(id).isEqualTo(tokenProvider.extractToValueFrom(token))
            );
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 2, 3, Long.MAX_VALUE})
        void refresh_token_이_정상적으로_발급된다(final long id) {

            var token = tokenProvider.createRefreshToken(id, Instant.now());

            assertAll(() -> assertThat(token).isNotNull(),
                    () -> assertThat(id).isEqualTo(tokenProvider.extractToValueFrom(token))
            );
        }
    }


    @Nested
    @DisplayName("토큰을 복호화하는 과정에서")
    class TokenDecoding {
        private final Long accessTokenExpiry = 7 * 24 * 60 * 60L;// 1주
        private final Long refreshTokenExpiry = 14 * 24 * 60 * 60L; // 2주

        @Test
        void 유효하지_않는_토큰의_경우_오류가_발생한다() {
            var token = "adsdsaasddsda";
            assertThatThrownBy(() -> tokenProvider.extractToValueFrom(token))
                    .isInstanceOf(BadJwtException.class);
        }


        @ParameterizedTest
        @ValueSource(ints = {60, 60 * 60, 60 * 60 * 24})
        void access_token_이_만료된지_7일이_지난_경우_오류가_발생한다(final int error) {
            // 7일 + 1분(오차)
            var issuanceTime = Instant.now()
                    .minusSeconds(accessTokenExpiry)
                    .minusSeconds(error);

            var token = tokenProvider.createAccessToken(1L, issuanceTime);

            assertThatThrownBy(() -> tokenProvider.extractToValueFrom(token))
                    .isInstanceOf(JwtValidationException.class);

        }

        @ParameterizedTest
        @ValueSource(ints = {60, 60 * 60, 60 * 60 * 24})
        void refresh_token_이_만료된지_7일이_지난_경우_오류가_발생한다(final int error) {
            // 14일 + error(오차)
            var issuanceTime = Instant.now()
                    .minusSeconds(refreshTokenExpiry)
                    .minusSeconds(error);

            var token = tokenProvider.createRefreshToken(1L, issuanceTime);

            assertThatThrownBy(() -> tokenProvider.extractToValueFrom(token))
                    .isInstanceOf(JwtValidationException.class);

        }
    }


}

class FakeTokenProviderGenerator {

    public static RSAKey rsaKey;

    static {
        try {
            rsaKey = new RSAKeyGenerator(2048).generate();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private static RSAPublicKey createRSAPublicKey() throws JOSEException {
        return rsaKey.toRSAPublicKey();
    }

    private static RSAPrivateKey createRSAPrivateKey() throws JOSEException {
        return rsaKey.toRSAPrivateKey();
    }

    public static JwtProvider createTokenProvider() throws JOSEException {

        var key = createRSAPublicKey();
        var priv = createRSAPrivateKey();

        var jwk = new RSAKey.Builder(key).privateKey(priv).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        var jwtEncoder = new NimbusJwtEncoder(jwks);
        var jwtDecoder = NimbusJwtDecoder.withPublicKey(key).build();

        return new JwtProvider(jwtEncoder, jwtDecoder);
    }
}

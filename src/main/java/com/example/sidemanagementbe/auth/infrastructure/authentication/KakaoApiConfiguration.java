package com.example.sidemanagementbe.auth.infrastructure.authentication;

import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class KakaoApiConfiguration {

    @Bean
    ErrorDecoder errorDecoder() {
        var stringDecoder = new StringDecoder();
        return (methodKey, response) -> {
            if (response.body() != null) {
                try {
                    var message = stringDecoder.decode(response, String.class).toString();
                    log.error("{}", message);
                    return new IllegalArgumentException("유효하지 않는 인증" + message);
                } catch (IOException e) {
                    log.error(methodKey + "Error Deserializing response body from failed feign request response.", e);
                }
            }
            return null;
        };
    }

}

package com.example.sidemanagementbe.web.security.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * OAuth2 클라이언트의 등록 정보를 관리하기 위한 설정 클래스 Spring Security의 OAuth2 클라이언트를 사용하여 외부 인증 서비스(예: Google, Facebook, Kakao 등)와 연동할
 * 때 사용됨
 */
@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@Conditional(ClientsConfiguredCondition.class)
public class OAuth2ClientRegistrationRepositoryConfiguration {

    private final OAuth2ClientProperties properties;

    @Autowired
    public OAuth2ClientRegistrationRepositoryConfiguration(OAuth2ClientProperties properties) {
        this.properties = properties;
    }

    /**
     * OAuth2 클라이언트 등록 정보를 관리하는 ClientRegistrationRepository 인터페이스의 구현체인 InMemoryClientRegistrationRepository를 생성.
     * InMemoryClientRegistrationRepository는 메모리에 클라이언트 등록 정보를 저장하고 필요할 때 해당 정보를 제공. 이를 통해 애플리케이션에서 OAuth2 클라이언트 등록 정보에
     * 쉽게 접근할 수 있음.
     */
    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
    public InMemoryClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>(
                OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(this.properties).values());
        return new InMemoryClientRegistrationRepository(registrations);
    }
}

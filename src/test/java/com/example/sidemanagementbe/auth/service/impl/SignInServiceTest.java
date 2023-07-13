package com.example.sidemanagementbe.auth.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.sidemanagementbe.auth.payload.request.SignInRequest;
import com.example.sidemanagementbe.login.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class SignInServiceTest {

    private SignInService signInService;

    @Autowired
    private MemberRepository memberRepository;
    @Mock
    private ExternalAuthenticationFacade externalAuthenticationFacade;
    @Mock
    private IssuanceTokenService issuanceTokenService;

    @BeforeEach
    void setup() {
        signInService = new SignInService(externalAuthenticationFacade, memberRepository, issuanceTokenService);
    }

    @Test
    void 등록되지_않은_사용자의_경우에는_에러가_발생합니다() {
        given(externalAuthenticationFacade.execute(any())).willReturn("id");
        assertThatThrownBy(() -> {
            signInService.execute(SignInRequest.builder()
                    .accessToken("access token")
                    .build());
        }).isInstanceOf(IllegalArgumentException.class);

    }

}
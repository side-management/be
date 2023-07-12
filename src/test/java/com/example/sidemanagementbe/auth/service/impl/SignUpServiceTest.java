package com.example.sidemanagementbe.auth.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {


    @InjectMocks
    private SignUpService signUpService;
    @Mock
    private ExternalAuthenticationFacade externalAuthenticationFacade;
    @Mock
    private RegisterMemberService registerMemberService;
    @Mock
    private IssuanceTokenService issuanceTokenService;


    @Test
    void api에서_존재하지_않는_토큰을_발급받는_경우_오류가_발생합니다() {
        given(externalAuthenticationFacade.execute(any())).willReturn(null);
        given(issuanceTokenService.execute(any())).willReturn(IssuanceTokenDto.builder().accessToken("access token")
                .refreshToken("refresh token")
                .build());

        assertThatThrownBy(() -> {
            signUpService.execute(SignUpRequest.builder()
                    .accessToken("access token")
                    .email("email")
                    .build());
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
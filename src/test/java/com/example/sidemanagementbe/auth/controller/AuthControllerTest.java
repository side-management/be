package com.example.sidemanagementbe.auth.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.sidemanagementbe.auth.payload.request.SignInRequest;
import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import com.example.sidemanagementbe.auth.payload.response.SignInResponse;
import com.example.sidemanagementbe.auth.payload.response.SignUpResponse;
import com.example.sidemanagementbe.auth.service.impl.IssuanceTokenService;
import com.example.sidemanagementbe.auth.service.impl.SignInService;
import com.example.sidemanagementbe.auth.service.impl.SignUpService;
import com.example.sidemanagementbe.web.jwt.util.JwtProvider;
import com.example.sidemanagementbe.web.security.config.SecurityConfig;
import com.example.sidemanagementbe.web.security.filter.CORSFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest({AuthController.class, CORSFilter.class, SecurityConfig.class})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SignUpService signUpService;
    @MockBean
    private SignInService signInService;
    @MockBean
    private IssuanceTokenService issuanceTokenService;
    @MockBean
    private JwtProvider jwtProvider;


    @Nested
    @DisplayName("회원 가입 api 단위 테스트에서")
    class SignUpApiTest {
        @Test
        void 토큰은_필수적으로_필요합니다() throws Exception {
            var signupRequest = SignUpRequest.builder()
                    .accessToken(null)
                    .email("email")
                    .nickname("nickname")
                    .build();
            getSignUpResultActions(signupRequest)
                    .andExpect(status().isBadRequest());
        }


        @Test
        void 성공적으로_응답을_내려준다() throws Exception {
            var signUpResponse = SignUpResponse.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .build();
            var signUpRequest = SignUpRequest.builder()
                    .accessToken("아무거나")
                    .email("email")
                    .nickname("nickname")
                    .build();
            given(signUpService.execute(any())).willReturn(signUpResponse);

            getSignUpResultActions(signUpRequest)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.accessToken").value(signUpResponse.getAccessToken()),
                            jsonPath("$.refreshToken").value(signUpResponse.getRefreshToken())
                    );
        }

        private ResultActions getSignUpResultActions(SignUpRequest signupRequest) throws Exception {
            return mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(signupRequest)))
                    .andDo(print());
        }
    }


    @Nested
    @DisplayName("로그인 api 단위 테스트에서")
    class SignInApiTest {
        @Test
        void 토큰은_필수적으로_필요합니다() throws Exception {
            var signInRequest = SignInRequest.builder()
                    .accessToken(null)
                    .build();
            getSignInResultActions(signInRequest)
                    .andExpectAll(status().isBadRequest());
        }


        @Test
        void 성공적으로_응답을_내려준다() throws Exception {
            var signInResponse = SignInResponse.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .build();
            var signInRequest = SignInRequest.builder()
                    .accessToken("아무거나")
                    .build();
            given(signInService.execute(any())).willReturn(signInResponse);

            getSignInResultActions(signInRequest)
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.accessToken").value(signInResponse.getAccessToken()),
                            jsonPath("$.refreshToken").value(signInResponse.getRefreshToken())
                    );
        }

        private ResultActions getSignInResultActions(SignInRequest signInRequest) throws Exception {
            return mockMvc.perform(post("/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(signInRequest)))
                    .andDo(print());
        }
    }


}
package com.example.sidemanagementbe.auth.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import com.example.sidemanagementbe.auth.payload.response.SignUpResponse;
import com.example.sidemanagementbe.auth.service.impl.SignUpService;
import com.example.sidemanagementbe.web.security.config.SecurityConfig;
import com.example.sidemanagementbe.web.security.util.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@WebMvcTest(controllers = {AuthController.class, SecurityConfig.class, JwtTokenProvider.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SignUpService signUpService;


    @Nested
    @DisplayName("회원 가입 api 단위 테스트에서")
    class SignUpApiTest {

        @Test
        void 토큰은_필수적으로_필요합니다() throws Exception {
            var signupRequest = SignUpRequest.builder()
                    .accessToken(null)
                    .build();
            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(signupRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void 성공적으로_응답을_내려준다() throws Exception {
            BDDMockito.given(signUpService.execute(any())).willReturn(SignUpResponse.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken").build());
            var signUpRequest = SignUpRequest.builder()
                    .accessToken("아무거나")
                    .build();

            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(signUpRequest)))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.accessToken").exists(),
                            jsonPath("$.refreshToken").exists(),
                            jsonPath("$.accessToken").value("accessToken"),
                            jsonPath("$.refreshToken").value("refreshToken")
                    );


        }

    }

}
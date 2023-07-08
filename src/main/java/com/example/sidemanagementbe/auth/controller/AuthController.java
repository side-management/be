package com.example.sidemanagementbe.auth.controller;


import com.example.sidemanagementbe.auth.payload.request.SignInRequest;
import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import com.example.sidemanagementbe.auth.payload.response.SignInResponse;
import com.example.sidemanagementbe.auth.payload.response.SignUpResponse;
import com.example.sidemanagementbe.auth.service.impl.SignInService;
import com.example.sidemanagementbe.auth.service.impl.SignUpService;
import javax.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final SignUpService signUpService;
    private final SignInService signInService;

    @PermitAll
    @PostMapping("/signup")
    public SignUpResponse signup(@Validated @RequestBody SignUpRequest signUpRequest) {
        return signUpService.execute(signUpRequest);
    }

    @PermitAll
    @PostMapping("/signin")
    public SignInResponse signin(@Validated @RequestBody SignInRequest signInRequest) {
        return signInService.execute(signInRequest);
    }
}

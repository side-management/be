package com.example.sidemanagementbe.auth.controller;


import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import com.example.sidemanagementbe.auth.payload.response.SignUpResponse;
import com.example.sidemanagementbe.auth.service.impl.SignUpService;
import javax.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PermitAll
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Validated @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(signUpService.execute(signUpRequest));
    }
}

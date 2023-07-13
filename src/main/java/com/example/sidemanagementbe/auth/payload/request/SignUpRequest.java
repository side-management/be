package com.example.sidemanagementbe.auth.payload.request;


import com.example.sidemanagementbe.login.entity.Gender;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SignUpRequest {
    @NotNull
    private String accessToken;
    @NotNull
    private String email;

    private String nickname;
    private String phoneNumber;
    private Gender gender;
    private String birthYear;
    private String provider;
    private String providerId;
    private String imageUrl;
}

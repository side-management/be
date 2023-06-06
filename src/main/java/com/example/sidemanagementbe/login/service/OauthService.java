package com.example.sidemanagementbe.login.service;

import com.example.sidemanagementbe.login.dto.KakaoUserInfo;
import com.example.sidemanagementbe.login.dto.LoginResponse;
import com.example.sidemanagementbe.login.dto.OAuth2UserInfo;
import com.example.sidemanagementbe.login.dto.OauthTokenResponse;
import com.example.sidemanagementbe.login.dto.RefreshTokenDto;
import com.example.sidemanagementbe.login.entity.Gender;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.login.entity.MemberRole;
import com.example.sidemanagementbe.login.repository.MemberRepository;
import com.example.sidemanagementbe.login.repository.RefreshTokenRepository;
import com.example.sidemanagementbe.login.security.util.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OauthService {
    private static final String BEARER_TYPE = "Bearer";

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(String providerName, String code) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);

        OauthTokenResponse tokenResponse = getToken(code, provider);
        Member member = getMemberProfile(providerName, tokenResponse, provider);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", String.valueOf(member.getId()));
        claims.put("role", member.getRole());
        claims.put("random UUID", UUID.randomUUID().toString());

        String accessToken = jwtTokenProvider.createAccessToken(claims);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        RefreshTokenDto tokenDto = RefreshTokenDto.builder()
                .memberId(member.getId())
                .refreshToken(refreshToken)
                .build();

        //Redis Cache에 refresh 토큰 저장
        refreshTokenRepository.save(tokenDto);

        return LoginResponse.builder()
                .id(member.getId())
                .imageUrl(member.getImageUrl())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .role(MemberRole.USER)
                .tokenType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    private OauthTokenResponse getToken(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", provider.getClientId());
        formData.add("redirect_uri", provider.getRedirectUriTemplate());
        formData.add("code", code);

        WebClient webClient = WebClient.builder()
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .build();

        return webClient.post()
                .uri(provider.getProviderDetails().getTokenUri())
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    private Member getMemberProfile(String providerName, OauthTokenResponse tokenResponse,
                                    ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        OAuth2UserInfo oauth2UserInfo = null;
        if (providerName.equals("kakao")) {
            oauth2UserInfo = new KakaoUserInfo(userAttributes);
        } else {
            log.info("허용되지 않은 접근 입니다.");
        }

        String provide = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String nickName = oauth2UserInfo.getNickName();
        String email = oauth2UserInfo.getEmail();
        Gender gender = oauth2UserInfo.getGender();
        String imageUrl = oauth2UserInfo.getImageUrl();

        Member memberEntity = memberRepository.findByEmail(email);

        if (memberEntity == null) {

            //일단 명시적으로 role를 USER 세팅, 차후 ADMIN 필요한 경우 추가할 것
            memberEntity = Member.createMember(email, nickName, gender, provide, providerId, imageUrl, MemberRole.USER);

            memberRepository.save(memberEntity);
        }

        return memberEntity;
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        WebClient client = WebClient.builder()
                .baseUrl(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .defaultHeaders(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .build();

        JsonNode response = client.get()
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // 필요한 사용자 속성 추출
        String email = response.path("kakao_account").path("email").asText();
        String nickname = response.path("properties").path("nickname").asText();
        String gender = response.path("kakao_account").path("gender").asText();
        String imageUrl = response.path("kakao_account").path("profile").path("profile_image_url").asText();
        String providerId = response.path("id").asText();
        String providerNm = "kakao";

        // 사용자 속성을 Map으로 반환
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("email", email);
        userAttributes.put("nickname", nickname);
        userAttributes.put("imageUrl", imageUrl);
        userAttributes.put("gender", gender);
        userAttributes.put("provider", providerNm);
        userAttributes.put("id", providerId);

        // ...

        return userAttributes;
    }


}

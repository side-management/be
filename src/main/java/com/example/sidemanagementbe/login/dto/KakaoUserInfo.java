package com.example.sidemanagementbe.login.dto;

import com.example.sidemanagementbe.login.entity.Gender;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> userAttributes) {
        this.attributes = userAttributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return (String) attributes.get("provider");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getNickName() {
        return (String) attributes.get("nickname");
    }

    @Override
    public Gender getGender() {
        String gender = (String) attributes.get("gender");

        if (gender.equals("male")) {
            return Gender.MAN;
        } else {
            return Gender.WOMAN;
        }
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("imageUrl");
    }


}

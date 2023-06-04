package com.example.sidemanagementbe.dto;

import com.example.sidemanagementbe.entity.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;


public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getNickName();

    String getEmail();

    Gender getGender();

    String getImageUrl();
}
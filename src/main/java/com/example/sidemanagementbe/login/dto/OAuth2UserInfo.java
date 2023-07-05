package com.example.sidemanagementbe.login.dto;

import com.example.sidemanagementbe.login.entity.Gender;


public interface OAuth2UserInfo {

    String getProvider();

    String getProviderId();

    String getNickname();

    String getEmail();

    Gender getGender();

    String getImageUrl();
}
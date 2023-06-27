package com.example.sidemanagementbe.login.entity;

import com.example.sidemanagementbe.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "saida_member")
@PrimaryKeyJoinColumn(name = "member_id")
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String nickname;

    @Embedded
    private Address address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    private String birthYear;

    private String provider;
    private String providerId;

    private String imageUrl;

    @Column(nullable = false, unique = true)
    private String email;


    @Enumerated(EnumType.STRING)
    private MemberRole role;


    public static Member createMember(String email, String nickname, Gender gender, String provider, String providerId,
                                      String imageUrl, MemberRole role) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .gender(gender)
                .provider(provider)
                .providerId(providerId)
                .imageUrl(imageUrl)
                .role(role)
                .build();
    }
}

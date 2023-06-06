package com.example.sidemanagementbe.login.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "email"})
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String nickName;

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

    @Builder
    public Member(Long id, String nickName, Address address, String phoneNumber, Gender gender, String birthYear,
                  String provider, String providerId, String imageUrl, String email, MemberRole role) {
        this.id = id;
        this.nickName = nickName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthYear = birthYear;
        this.provider = provider;
        this.providerId = providerId;
        this.imageUrl = imageUrl;
        this.email = email;
        this.role = role;
    }

    public static Member createMember(String email, String nickName, Gender gender, String provider, String providerId,
                                      String imageUrl, MemberRole role) {
        return Member.builder()
                .email(email)
                .nickName(nickName)
                .gender(gender)
                .provider(provider)
                .providerId(providerId)
                .imageUrl(imageUrl)
                .role(role)
                .build();
    }
}

package com.example.sidemanagementbe.login.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {
    @Id
    private String refreshToken;
    private Long memberId;

    // 생성자, getter, setter 등 필요한 메소드 작성
}

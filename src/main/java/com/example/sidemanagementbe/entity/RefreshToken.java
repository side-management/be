package com.example.sidemanagementbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Id;

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

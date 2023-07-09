package com.example.sidemanagementbe.auth.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.login.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class RegisterMemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    private RegisterMemberService registerMemberService;

    private Member member1;

    @BeforeEach
    void setup() {
        this.registerMemberService = new RegisterMemberService(memberRepository);
        member1 = Member.builder()
                .email("email")
                .providerId("id")
                .nickname("nickname").build();
    }

    @Test
    void 이미_외부인증을_통해서_등록된_사용자는_오류가_발생한다() {
        assertThatThrownBy(() -> {
            memberRepository.save(member1);
            this.registerMemberService.execute("id", SignUpRequest.builder()
                    .email("email")
                    .build());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
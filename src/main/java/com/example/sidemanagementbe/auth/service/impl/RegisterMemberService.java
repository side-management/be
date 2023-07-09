package com.example.sidemanagementbe.auth.service.impl;


import com.example.sidemanagementbe.auth.payload.request.SignUpRequest;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.login.repository.MemberRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMemberService {
    private final MemberRepository memberRepository;


    @Transactional
    public Long execute(String socialId, SignUpRequest signUpRequest) {
        memberRepository.findByProviderId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("이미 존재하는 사용자입니다."));

        var member = Member.builder()
                .email(signUpRequest.getEmail())
                .build();

        var savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}

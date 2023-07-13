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
    public Long execute(final String socialId, SignUpRequest signUpRequest) {
        memberRepository.findByNicknameOrEmail(signUpRequest.getNickname(), signUpRequest.getEmail())
                .ifPresent((member) -> {
                    throw new IllegalArgumentException("닉네임 혹은 이메일이 이미 등록되어 있습니다.");
                });
        var member = Member.builder()
                .email(signUpRequest.getEmail())
                .providerId(socialId)
                .nickname("test")
                .build();
        var savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}

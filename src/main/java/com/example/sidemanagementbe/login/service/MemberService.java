package com.example.sidemanagementbe.login.service;

import com.example.sidemanagementbe.login.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public String getMemberNickname(Long memberId) {
        return memberRepository.findNicknameById(memberId);
    }


}

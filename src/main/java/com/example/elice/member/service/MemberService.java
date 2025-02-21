package com.example.elice.member.service;

import com.example.elice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean isFirstLogin(String email) {
        return memberRepository.findByEmail(email).isEmpty();
    }

}

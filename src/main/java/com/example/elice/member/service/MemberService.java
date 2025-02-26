package com.example.elice.member.service;

import com.example.elice.common.auth.aop.dto.CurrentMemberIdRequest;
import com.example.elice.member.domain.Member;
import com.example.elice.member.domain.Survey;
import com.example.elice.member.domain.dto.request.SurveyRequest;
import com.example.elice.member.exception.MemberNotFoundException;
import com.example.elice.member.repository.MemberRepository;
import com.example.elice.member.repository.SurveyRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;

    public boolean isFirstLogin(String email) {
        return memberRepository.findByEmail(email).isEmpty();
    }

    @Transactional
    public void surveyFirstInput(SurveyRequest request, CurrentMemberIdRequest currentMemberIdRequest) {

        Long userId = currentMemberIdRequest.getMemberId();
        Member member = memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
        System.out.println(userId);
        System.out.println(member);
        Survey survey = Survey.builder()
                .memberId(member.getId())
                .age(request.getAge())
                .job(request.getJob())
                .useObject(request.getUseObject())
                .build();

        surveyRepository.save(survey);
    }

}

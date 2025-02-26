package com.example.elice.member.controller;

import com.example.elice.common.auth.aop.AssignCurrentMemberId;
import com.example.elice.common.auth.aop.dto.CurrentMemberIdRequest;
import com.example.elice.member.domain.dto.request.SurveyRequest;
import com.example.elice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/survey")
    @AssignCurrentMemberId
    public ResponseEntity<Void> surveyFirstInput(@RequestBody SurveyRequest surveyRequest, CurrentMemberIdRequest currentMemberIdRequest) {
        memberService.surveyFirstInput(surveyRequest, currentMemberIdRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}

package com.example.elice.member.controller;

import com.example.elice.common.auth.aop.AssignCurrentMemberId;
import com.example.elice.common.auth.aop.dto.CurrentMemberIdRequest;
import com.example.elice.member.domain.dto.request.SurveyRequest;
import com.example.elice.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "회원 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/survey")
    @Operation(summary = "설문조사 입력", description = "설문조사를 입력을 합니다.")
    @AssignCurrentMemberId
    public ResponseEntity<Void> surveyFirstInput(@RequestBody SurveyRequest surveyRequest, CurrentMemberIdRequest currentMemberIdRequest) {
        memberService.surveyFirstInput(surveyRequest, currentMemberIdRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}

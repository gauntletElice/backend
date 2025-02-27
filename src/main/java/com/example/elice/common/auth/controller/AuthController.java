package com.example.elice.common.auth.controller;

import com.example.elice.common.auth.dto.LoginResponse;
import com.example.elice.common.auth.jwt.info.TokenResponse;
import com.example.elice.common.auth.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 합니다.")
    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> login(@RequestParam(name= "code") String code) throws JsonProcessingException {
        LoginResponse loginResponse = authService.kakaoLogin(code);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Operation(summary = "TEST 전용 | 카카오 로그인(Postman)", description = "카카오 로그인(Postman)을 합니다.")
    @PostMapping("/login/kakao/postman")
    public ResponseEntity<LoginResponse> loginForPostman(@RequestParam(name= "code") String code) throws JsonProcessingException {
        LoginResponse loginResponse = authService.kakaoLoginForPostman(code);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.reissueAccessToken(request), HttpStatus.OK);
    }
}

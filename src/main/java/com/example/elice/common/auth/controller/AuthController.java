package com.example.elice.common.auth.controller;

import com.example.elice.common.auth.jwt.info.LoginResponse;
import com.example.elice.common.auth.jwt.info.TokenResponse;
import com.example.elice.common.auth.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> login(@RequestParam(name= "code") String code) throws JsonProcessingException {
        LoginResponse loginResponse = authService.kakaoLogin(code);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/login/kakao/postman")
    public ResponseEntity<LoginResponse> loginForPostman(@RequestParam(name= "code") String code) throws JsonProcessingException {
        LoginResponse loginResponse = authService.kakaoLoginForPostman(code);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.reissueAccessToken(request), HttpStatus.OK);
    }
}

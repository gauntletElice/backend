package com.example.elice.common.auth.handler;

import com.example.elice.common.auth.exception.AccessDeniedException;
import com.example.elice.common.auth.exception.AccessTokenExpiredException;
import com.example.elice.common.auth.exception.AuthenticationEntryPointException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Exception API", description = "인증 예외 처리 API")
@RestController
@RequestMapping("/api/exception")
public class AuthExceptionController {

    @Operation(summary = "접근 거부 예외", description = "접근 거부 예외를 발생시킵니다.")
    @GetMapping("/access-denied")
    public void accessDeniedException() {
        throw new AccessDeniedException();
    }

    @Operation(summary = "인증 entry-point 예외", description = "인증 진입점 예외를 발생시킵니다.")
    @GetMapping("/entry-point")
    public void authenticateException() {
        throw new AuthenticationEntryPointException();
    }

    @Operation(summary = "access token 만료 예외", description = "액세스 토큰 만료 예외를 발생시킵니다.")
    @GetMapping("/access-token-expired")
    public void accessTokenExpiredException() {
        throw new AccessTokenExpiredException();
    }
}

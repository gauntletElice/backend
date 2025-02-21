package com.example.elice.common.auth.handler;

import com.example.elice.common.auth.exception.AccessDeniedException;
import com.example.elice.common.auth.exception.AccessTokenExpiredException;
import com.example.elice.common.auth.exception.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception")
public class AuthExceptionController {

    @GetMapping("/access-denied")
    public void accessDeniedException() {
        throw new AccessDeniedException();
    }

    @GetMapping("/entry-point")
    public void authenticateException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/access-token-expired")
    public void accessTokenExpiredException() {
        throw new AccessTokenExpiredException();
    }
}

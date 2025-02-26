package com.example.elice.member.exception.handler;

import com.example.elice.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class MemberExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity memberNotFoundException() {
        return new ResponseEntity("존재하지 않는 회원입니다.", httpHeaders, HttpStatus.NOT_FOUND);
    }

}

package com.example.elice.fortuneCookie.controller;

import com.example.elice.fortuneCookie.domain.dto.FortuneCookieResponse;
import com.example.elice.fortuneCookie.service.FortuneCookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Fortune Cookie API", description = "포춘쿠키 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fortune-cookie")
public class FortuneCookieController {

    private final FortuneCookieService fortuneCookieService;

    @Operation(summary = "포춘쿠키 조회", description = "포춘쿠키를 조회합니다.")
    @GetMapping
    public ResponseEntity<FortuneCookieResponse> getFortuneCookie() {
        FortuneCookieResponse fortuneCookieResponse = fortuneCookieService.findByCookie();
        return new ResponseEntity<>(fortuneCookieResponse, HttpStatus.OK);
    }

}

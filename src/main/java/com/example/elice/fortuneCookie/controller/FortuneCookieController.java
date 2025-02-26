package com.example.elice.fortuneCookie.controller;

import com.example.elice.fortuneCookie.domain.dto.FortuneCookieResponse;
import com.example.elice.fortuneCookie.service.FortuneCookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fortune-cookie")
public class FortuneCookieController {

    private final FortuneCookieService fortuneCookieService;

    @GetMapping
    public ResponseEntity<FortuneCookieResponse> getFortuneCookie() {
        FortuneCookieResponse fortuneCookieResponse = fortuneCookieService.findByCookie();
        return new ResponseEntity<>(fortuneCookieResponse, HttpStatus.OK);
    }

}

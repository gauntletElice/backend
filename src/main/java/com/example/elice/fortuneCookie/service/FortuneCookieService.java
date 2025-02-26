package com.example.elice.fortuneCookie.service;

import com.example.elice.fortuneCookie.domain.FortuneCookie;
import com.example.elice.fortuneCookie.domain.dto.FortuneCookieResponse;
import com.example.elice.fortuneCookie.repository.FortuneCookieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FortuneCookieService {
    private final FortuneCookieRepository fortuneCookieRepository;

    public FortuneCookieResponse findByCookie() {
        FortuneCookie fortuneCookie = fortuneCookieRepository.findRandomCookie();
        return FortuneCookieResponse.getFortuneCookieResponse(fortuneCookie.getMessage());
    }

    @Transactional
    public FortuneCookie saveFortune(String message) {
        FortuneCookie fortuneCookie = FortuneCookie.builder()
                .message(message).build();
        return fortuneCookieRepository.save(fortuneCookie);
    }

}

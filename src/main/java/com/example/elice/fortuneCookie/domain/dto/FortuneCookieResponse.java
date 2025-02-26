package com.example.elice.fortuneCookie.domain.dto;

import lombok.Getter;

@Getter
public class FortuneCookieResponse {

    private final String message;

    private FortuneCookieResponse(String message) {
        this.message = message;
    }

    public static FortuneCookieResponse getFortuneCookieResponse(String message) {
        return new FortuneCookieResponse(message);
    }

}

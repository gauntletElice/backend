package com.example.elice.common.auth.jwt.info;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final TokenResponse tokenResponse;
    private final boolean isFirstLogin;

    private LoginResponse(final String accessToken, final String refreshToken, final boolean isFirstLogin) {
        this.tokenResponse = new TokenResponse(accessToken, refreshToken);
        this.isFirstLogin = isFirstLogin;
    }

    public static LoginResponse of(final String accessToken, final String refreshToken, final boolean isFirstLogin) {
        return new LoginResponse(accessToken, refreshToken, isFirstLogin);
    }
}

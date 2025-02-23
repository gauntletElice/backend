package com.example.elice.common.auth.oauth2;

import com.example.elice.common.global.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class KakaoUserInfo {

    private Map<String, Object> attributes;
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        Map<String, Object> account = ObjectAbstract();
        return (String) account.get(Constants.EMAIL);
    }

    public String getProfile() {
        Map<String, Object> account = ObjectAbstract();
        return (String) account.get(Constants.PROFILE);
    }

    private Map<String, Object> ObjectAbstract() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
        };
        Object kakaoAccount = attributes.get(Constants.KAKAO_ACCOUNT);
        return objectMapper.convertValue(kakaoAccount, typeReferencer);
    }

    public String getNickname() {

        Map<String, Object> account = ObjectAbstract();
        return (String) account.get(Constants.NICKNAME);
    }

}

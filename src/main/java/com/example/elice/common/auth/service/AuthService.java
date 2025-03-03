package com.example.elice.common.auth.service;

import com.example.elice.common.auth.dto.KakaoLoginRequest;
import com.example.elice.common.auth.exception.InvalidAuthCodeException;
import com.example.elice.common.auth.exception.ReissueFailException;
import com.example.elice.common.auth.dto.LoginResponse;
import com.example.elice.common.auth.jwt.info.RefreshToken;
import com.example.elice.common.auth.jwt.info.TokenProvider;
import com.example.elice.common.auth.jwt.info.TokenResponse;
import com.example.elice.common.auth.repository.RefreshTokenRedisRepository;
import com.example.elice.member.domain.Member;
import com.example.elice.member.repository.MemberRepository;
import com.example.elice.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.Pair;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private static final String REFRESH_HEADER = "RefreshToken";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    @Transactional
    public LoginResponse kakaoLogin(final String code) throws JsonProcessingException {
        String token = getToken(code);
        KakaoLoginRequest request = getKakaoUserInfo(token);

        Pair<Member, Boolean> pairMemberInfo= saveIfNonExist(request);
        Member member = pairMemberInfo.getFirst();
        boolean inNewMember = pairMemberInfo.getSecond();

        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(member.getId()),
                member.getEmail(),
                member.getRole().name());

        saveRefreshTokenOnRedis(member, tokenResponse);

        if (inNewMember) {
            log.info("신규 회원입니다. : email={}", member.getEmail());
            return LoginResponse.of(tokenResponse.accessToken(), tokenResponse.refreshToken(), true);
        }
        log.info("기존 회원입니다. : email={}", member.getEmail());
        return LoginResponse.of(tokenResponse.accessToken(), tokenResponse.refreshToken(), false);
    }

    @Transactional
    public LoginResponse kakaoLoginForPostman(final String code) throws JsonProcessingException {
        KakaoLoginRequest request = getKakaoUserInfo(code);

        Pair<Member, Boolean> pairMemberInfo= saveIfNonExist(request);
        Member member = pairMemberInfo.getFirst();
        boolean inNewMember = pairMemberInfo.getSecond();

        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(member.getId()),
                member.getEmail(),
                member.getRole().name());

        saveRefreshTokenOnRedis(member, tokenResponse);

        if (inNewMember) {
            log.info("신규 회원입니다. : email={}", member.getEmail());
            return LoginResponse.of(tokenResponse.accessToken(), tokenResponse.refreshToken(), true);
        }
        log.info("기존 회원입니다. : email={}", member.getEmail());
        return LoginResponse.of(tokenResponse.accessToken(), tokenResponse.refreshToken(), false);
    }


    private String getToken(final String code) throws JsonProcessingException {
        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private KakaoLoginRequest getKakaoUserInfo(final String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode kakaoAccountNode = jsonNode.path("kakao_account");
            JsonNode profileNode = kakaoAccountNode.path("profile");

            String email = kakaoAccountNode.path("email").asText(null);
            String profile = profileNode.path("profile_image_url").asText(null);
            String nickname = profileNode.path("nickname").asText(null);

            return new KakaoLoginRequest(email,profile,nickname);
        } catch (HttpClientErrorException e) {
            throw new InvalidAuthCodeException();
        }
    }

    private void saveRefreshTokenOnRedis(final Member member, final TokenResponse response) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().name()));
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(member.getId())
                .email(member.getEmail())
                .authorities(simpleGrantedAuthorities)
                .refreshToken(response.refreshToken())
                .build());
    }

    @Transactional
    public Pair<Member, Boolean> saveIfNonExist(final KakaoLoginRequest request) {
        return memberRepository.findByEmail(request.getEmail())
                .map(member -> Pair.of(member,false))
                .orElseGet(() -> {
                        Member newMember = memberRepository.save(Member.createFirstLoginMember(request.getEmail(), request.getNickname(), request.getProfile())
                    );
        return Pair.of(newMember,true);
                });
    }

    public TokenResponse reissueAccessToken(final HttpServletRequest request) {
        String refreshToken = getTokenFromHeader(request, REFRESH_HEADER);

        if (!tokenProvider.validate(refreshToken) || !tokenProvider.validateExpire(refreshToken)) {
            throw new ReissueFailException();
        }

        RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        TokenResponse tokenResponse = tokenProvider.createToken(
                String.valueOf(findToken.getId()),
                findToken.getEmail(),
                findToken.getAuthority());

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(findToken.getId())
                .email(findToken.getEmail())
                .authorities(findToken.getAuthorities())
                .refreshToken(tokenResponse.refreshToken())
                .build());

        SecurityContextHolder.getContext()
                .setAuthentication(tokenProvider.getAuthentication(tokenResponse.accessToken()));

        return tokenResponse;
    }

    private String getTokenFromHeader(final HttpServletRequest request, final String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }
}

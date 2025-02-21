package com.example.elice.common.auth.oauth2;

import com.example.elice.member.domain.Member;
import com.example.elice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.createFirstLoginMember(kakaoUserInfo.getEmail())
                        )
                );
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(member.getRole().name());


        return new KakaoMemberDetails(member.getId(),
                String.valueOf(member.getEmail()),
                Collections.singletonList(simpleGrantedAuthority),
                oAuth2User.getAttributes());
    }
}
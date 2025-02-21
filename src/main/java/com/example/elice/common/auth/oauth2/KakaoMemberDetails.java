package com.example.elice.common.auth.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class KakaoMemberDetails implements OAuth2User {
    private final Long id;
    private final String email;
    private final List<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}

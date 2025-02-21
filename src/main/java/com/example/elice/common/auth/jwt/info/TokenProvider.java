package com.example.elice.common.auth.jwt.info;

import com.example.elice.common.auth.oauth2.KakaoMemberDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final String AUTH_ID = "ID";
    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_EMAIL = "EMAIL";

    private final String secretKey;
    private final long accessTokenValidityMilliSeconds;
    private final long refreshTokenValidityMilliSeconds;
    private Key secretkey;

    public TokenProvider(@Value("${jwt.secret_key}") String secretKey,
                         @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValiditySeconds,
                         @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValiditySeconds) {
        this.secretKey = secretKey;
        this.accessTokenValidityMilliSeconds = accessTokenValiditySeconds * 1000;
        this.refreshTokenValidityMilliSeconds = refreshTokenValiditySeconds * 1000;
    }

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretkey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public TokenResponse createToken(String memberId, String email, String role) {
        long now = (new Date()).getTime();

        Date accessValidity = new Date(now + this.accessTokenValidityMilliSeconds);
        Date refreshValidity = new Date(now + this.refreshTokenValidityMilliSeconds);

        String accessToken = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, memberId))
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_KEY, role))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity)
                .compact();

        String refreshToken = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, memberId))
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_KEY, role))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        return TokenResponse.of(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> authorities = Arrays.asList(claims.get(AUTH_KEY)
                .toString()
                .split(","));

        List<? extends GrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(auth -> new SimpleGrantedAuthority(auth))
                .collect(Collectors.toList());

        KakaoMemberDetails principal = new KakaoMemberDetails(Long.parseLong((String) claims.get(AUTH_ID)),
                (String) claims.get(AUTH_EMAIL),
                simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean validateExpire(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}

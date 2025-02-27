package com.example.elice.common.auth.jwt;

import com.example.elice.common.auth.jwt.info.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String ACCESS_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isRequestPassURI(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getTokenFromHeader(request, ACCESS_HEADER);

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isExpired = tokenProvider.validateExpire(accessToken);
        boolean isValid = tokenProvider.validate(accessToken);

        if (!isExpired && isValid) {
            String redirectUrl = "https://" + request.getServerName() + "/api/exception/access-token-expired";
            response.sendRedirect(redirectUrl);
            return;
        }

        if (isExpired && isValid) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }

        filterChain.doFilter(request, response);
    }

    private static boolean isRequestPassURI(HttpServletRequest request) {
        if (request.getRequestURI().equals("/")) {
            return true;
        }

        if (request.getRequestURI().startsWith("/api/auth")) {
            return true;
        }

        if (request.getRequestURI().startsWith("/api/exception")) {
            return true;
        }
        if (request.getRequestURI().startsWith("/favicon.ico")) {
            return true;
        }
        if (request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")) {
            return true;
        }
        return false;
    }

    private String getTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }
}

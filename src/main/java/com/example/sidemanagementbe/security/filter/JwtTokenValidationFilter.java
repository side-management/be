package com.example.sidemanagementbe.security.filter;

import com.example.sidemanagementbe.dto.KakaoUserInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtTokenValidationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final RedisTemplate<String, String> redisTemplate;
    private final String secretKey;
    private final long tokenExpirationMillis;

    public JwtTokenValidationFilter(RedisTemplate<String, String> redisTemplate, String secretKey, long tokenExpirationMillis) {
        this.redisTemplate = redisTemplate;
        this.secretKey = secretKey;
        this.tokenExpirationMillis = tokenExpirationMillis;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------------JwtTokenValidationFilter CALL--------------");
        if (shouldSkipValidation(request)) {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String token = extractToken(request);

        try {
            if (token != null && validateToken(token)) {
//                Authentication authentication = getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException expiredJwtException) {
            // Handle expired token exception
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token expired");
            response.getWriter().flush();
            return;
        } catch (JwtException jwtException) {
            // Handle invalid token exception
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipValidation(HttpServletRequest request) {
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/access-token", "POST");
        return requestMatcher.matches(request);
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private boolean validateToken(String token) {
        // Check if token is present in Redis cache
        if (redisTemplate.hasKey(token)) {
            return true;
        }

        try {
            // Validate token using secret key
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // Add token to Redis cache with expiration time
            redisTemplate.opsForValue().set(token, "valid", tokenExpirationMillis, TimeUnit.MILLISECONDS);
            return true;
        } catch (JwtException jwtException) {
            return false;
        }
    }

    private Authentication getAuthentication(String token) {
        // 토큰을 이용하여 카카오 API로 사용자 정보를 조회하는 로직을 구현해야 합니다.
        // 여기에서는 간략하게 예시를 드리겠습니다.

        // 토큰을 이용하여 카카오 API 호출하여 사용자 정보 조회
        KakaoUserInfo userInfo = new KakaoUserInfo(new HashMap<>());

        // 카카오에서 반환된 사용자 정보를 기반으로 Spring Security의 User 객체 생성
        UserDetails userDetails = createUserDetails(userInfo);

        // 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    private UserDetails createUserDetails(KakaoUserInfo userInfo) {
        // 카카오에서 반환된 사용자 정보를 기반으로 UserDetails 객체를 생성하는 로직을 구현해야 합니다.
        // 여기에서는 간략하게 예시를 드리겠습니다.

        // 사용자의 권한 정보 설정 (여기서는 ROLE_USER로 가정)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // 사용자의 ID를 기반으로 UserDetails 객체 생성
        return new User(userInfo.getEmail(), "", authorities);
    }
}

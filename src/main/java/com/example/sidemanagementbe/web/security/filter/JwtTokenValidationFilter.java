package com.example.sidemanagementbe.web.security.filter;

import com.example.sidemanagementbe.login.dto.KakaoUserInfo;
import com.example.sidemanagementbe.web.security.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtTokenValidationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenValidationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //스킵해야 할 url 지정
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

        log.info("-------------------JwtTokenValidationFilter CALL-------------------");
        log.info("-------------------request URI: " + request.getRequestURI() + "---------------");

        String token = extractToken(request);
        Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(token));

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 권한 부여
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        memberId, null, List.of(new SimpleGrantedAuthority("USER")));

                // Detail을 넣어줌
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("[+] Token in SecurityContextHolder");
                filterChain.doFilter(request, response);
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
        String path = request.getRequestURI();

        return requestMatcher.matches(request) || path.startsWith("/login")
                || path.startsWith("/swagger") || path.startsWith("/v2") || path.startsWith("/v3") || path.startsWith(
                "/favicon.ico") || path.startsWith("/h2-console");
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
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

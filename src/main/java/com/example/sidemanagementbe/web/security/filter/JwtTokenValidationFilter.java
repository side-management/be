package com.example.sidemanagementbe.web.security.filter;

import com.example.sidemanagementbe.web.jwt.util.JwtProvider;
import com.example.sidemanagementbe.web.security.exception.JwtExpiredTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenValidationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private final JwtProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("-------------------JwtTokenValidationFilter CALL-------------------");
        log.info("-------------------request URI: " + request.getRequestURI() + "---------------");
        var token = extractToken(request);
        if (Objects.isNull(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("token:" + token);
        var memberId = jwtTokenProvider.extractToValueFrom(token);

        try {
            if (!jwtTokenProvider.validateToken(token)) {
                throw new JwtExpiredTokenException("Access token has expired");
            }

            // 권한 부여
            var authenticationToken = new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority("USER")));
            // Detail을 넣어줌
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("[+] Token in SecurityContextHolder");
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException expiredJwtException) {
            // Handle expired token exception
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token expired");
            response.getWriter().flush();
        } catch (JwtException jwtException) {
            // Handle invalid token exception
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token");
            response.getWriter().flush();
        }
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}

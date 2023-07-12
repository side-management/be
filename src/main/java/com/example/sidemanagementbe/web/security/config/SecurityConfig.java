package com.example.sidemanagementbe.web.security.config;


import com.example.sidemanagementbe.web.jwt.util.JwtProvider;
import com.example.sidemanagementbe.web.security.filter.JwtTokenValidationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false)
@Slf4j
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    JwtTokenValidationFilter jwtTokenValidationFilter() {
        return new JwtTokenValidationFilter(jwtProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http 기본 설정
        http.csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 명시적으로 허용할 url 등록
        http.authorizeRequests()
                .antMatchers("/favicon.ico", "/login/oauth/kakao/**", "/h2-console/**").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
                        "/v2/api-docs", "/webjars/**", "/ws", "/ws/**").permitAll()
                .and()// 세션을 사용하지 않고 jwt 토큰을 활용

        // 필터
        http.addFilterBefore(jwtTokenValidationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

       /*  http
//                .authorizeRequests()
//                .antMatchers("/favicon.ico", "/login/oauth/kakao/**", "/h2-console/**").permitAll()
//                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
//                        "/v2/api-docs", "/webjars/**", "/ws", "/ws/**").permitAll()
//                .antMatchers("/auth/**").permitAll()
//                //.anyRequest().authenticated()
//                .and()
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .addFilterBefore(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().build();*/

    }

    @Bean
    public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("Swagger Realm");
        return entryPoint;
    }


}

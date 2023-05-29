package com.example.sidemanagementbe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


/**
 * SecurityConfig 클래스는 시큐리티 관련 기능을 쉽게 설정하기 위해서 WebSecurityConfigurerAdapter라는 클래스를 상속으로 처리한다.
 * WebSecurityConfigurerAdapter 클래스는 주로 override를 통해서 여러 설정을 조정하게 된다.
 * 하지만 이는 Deprecated 됐음으로 이제는 SecurityFilterChain를 빈으로 등록해서 사용하는 것을 권장한다.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers()
                .frameOptions().disable()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable();
        return http.build();
    }


}

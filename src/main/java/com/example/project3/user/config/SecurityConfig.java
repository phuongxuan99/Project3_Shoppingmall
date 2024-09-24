package com.example.project3.user.config;

import com.example.project3.user.entity.CustomUserDetails;
import com.example.project3.user.jwt.JwtTokenFilter;
import com.example.project3.user.jwt.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    private final JwtTokenUtils tokenUtils;
    private final JwtTokenFilter tokenFilter;
    private final UserDetailsService detailsService;

    public SecurityConfig(JwtTokenUtils tokenUtils, JwtTokenFilter tokenFilter, UserDetailsService detailsService) {
        this.tokenUtils = tokenUtils;
        this.tokenFilter = tokenFilter;
        this.detailsService = detailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/login",
                                "/users/register")
                        .permitAll()  // 로그인 및 회원가입은 모든 사용자 접근 허용
                        .requestMatchers(HttpMethod.PUT, "/users/update")
                        .hasAnyRole("USER", "BUSINESS_USER", "ADMIN")  // 사용자 정보 업데이트는 특정 역할만 허용
                        .requestMatchers("/users/business-requests")
                        .authenticated()  // 비즈니스 신청 요청은 인증된 사용자만 접근
                        .requestMatchers("/users/reject-business/**",
                                "/users/approve-business/**")
                        .hasRole("ADMIN")


                        //shops
                        .requestMatchers("shops").authenticated()
                        .requestMatchers("shops/**").authenticated()
                        .requestMatchers("/shops/**/approve",
                                "/shops/**/reject",
                                "/shops/**/request-closure",
                                "/shops/**/close" ).hasRole("ADMIN")
                        .requestMatchers("shops/search").permitAll()
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )

                // 세션 관리 - JWT를 사용하므로 무상태(stateless) 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT 필터 추가
                .addFilterBefore(
                        new JwtTokenFilter(
                                tokenUtils,
                                detailsService
                        ),
                        AuthorizationFilter.class
                );

        return http.build();
    }
}

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth ->
//                        auth
//                                .requestMatchers(
//                                        "/users/login",
//                                        "/users/register")
//                                .permitAll()
//                                .requestMatchers(HttpMethod.PUT, "/update")
//                                .hasAnyRole("USER", "ADMIN")  // USER 또는 ADMIN 역할을 허용
//                                .requestMatchers("/users/business-requests")
//                                .authenticated()
//                                .requestMatchers(
//                                        "/users/reject-business/**",
//                                        "/users/approve-business/**")
//                                .hasRole("ADMIN")
//                                .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .addFilterBefore(new JwtTokenFilter(tokenUtils, detailsService), AuthorizationFilter.class);
//
//        return http.build();
//    }
//}
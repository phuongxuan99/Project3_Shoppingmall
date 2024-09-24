package com.example.project3.user.config;

import com.example.project3.user.entity.CustomUserDetails;
import com.example.project3.user.jwt.JwtTokenFilter;
import com.example.project3.user.jwt.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/users/login",
                                "/users/register")
                                .permitAll()
                                .requestMatchers("/users/business-requests")
                                .authenticated()
                                .requestMatchers("/users/reject-business/**",
                                        "/users/approve-business/**")
                                .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(tokenUtils, detailsService), AuthorizationFilter.class);

        return http.build();
    }
}
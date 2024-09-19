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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtTokenFilter jwtTokenFilter) throws Exception {
        // URL에 따라 접근 권한을 조정하는 방법
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/users/login",
                                    "/token/validate-test",
                                    "/users/register",
                                    "/users/apply-business")
                            .permitAll();

                    // 익명 사용자만 접근 가능
                    auth.requestMatchers(
                                    "/users/approve-business/",
                                    "/users/business-requests",
                                    "/users/reject-business/")
                            .hasRole("ADMIN");  // 관리자만 접근 가능하도록 수정

                    auth.requestMatchers(
                                    "/articles/**",
                                    "/token/is-authenticated")
                            .authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        return http.build();
    }

//        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();


}
//    private final JwtTokenUtils jwtTokenUtils;
//    private final CustomUserDetails customUserDetails;
//    private final JwtTokenFilter jwtTokenFilter;
//
//    public SecurityConfig(JwtTokenUtils jwtTokenUtils, CustomUserDetails customUserDetails, JwtTokenFilter jwtTokenFilter) {
//        this.jwtTokenUtils = jwtTokenUtils;
//        this.customUserDetails = customUserDetails;
//        this.jwtTokenFilter = jwtTokenFilter;
//    }
//
//}
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//            .csrf(AbstractHttpConfigurer::disable)
//            .authorizeHttpRequests(auth -> {
//                auth.requestMatchers("/users/login",
//                                "/users/register")
//                        .permitAll() ;// 로그인 요청은 인증 없이 접근 가능
////                        .anyRequest()
////                        .authenticated(); // 나머지 요청은 인증 필요
//            })
//            .formLogin(formLogin -> formLogin
//                    .loginPage("/users/login")
//                    .permitAll()
//            )
////            .logout(logout -> logout
////                    .logoutUrl("/users/logout")
////                    .logoutSuccessUrl("/users/login")
////            )
//            .addFilterBefore(new JwtTokenFilter(jwtTokenUtils, customUserDetails), UsernamePasswordAuthenticationFilter.class);
//
//    return http.build();
//}
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}

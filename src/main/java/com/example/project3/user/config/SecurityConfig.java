package com.example.project3.user.config;

import com.example.project3.user.entity.CustomUserDetails;
import com.example.project3.user.jwt.JwtTokenFilter;
import com.example.project3.user.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
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

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService detailsService;



    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/users/signin",
                                    "/users/signup"
                            )
                            .anonymous();
                    auth.requestMatchers(
                                    "/users/details",
                                    "/users/profile",
                                    "/users/get-user-info"
                            )
                            .authenticated();
                    auth.requestMatchers(
                                    "/admin/**"
                            )
                            .hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET,
                            "/shops/**/products")
                            .hasAnyRole("ADMIN", "OWNER");

                    auth.requestMatchers(HttpMethod.POST,
                            "/shops/**/products")
                            .hasAnyRole("ADMIN", "OWNER");
                    auth.requestMatchers(HttpMethod.PUT,
                            "/shops/**/products/**")
                            .hasAnyRole("ADMIN", "OWNER");
                    auth.requestMatchers(HttpMethod.DELETE,
                            "/shops/**/products/**")
                            .hasAnyRole("ADMIN", "OWNER");

                    auth.requestMatchers(HttpMethod.POST,
                            "/shop/purchase-requests")
                            .hasAnyRole("USER", "ADMIN");

                    auth.requestMatchers(HttpMethod.POST,
                            "/shop/purchase-requests/**/accept")
                            .hasAnyRole("ADMIN", "OWNER");
                    auth.requestMatchers(HttpMethod.POST,
                            "/shop/purchase-requests/**/cancel")
                            .hasAnyRole("ADMIN", "OWNER");

                    auth.requestMatchers(HttpMethod.POST, "/shops")
                            .hasAnyRole("ADMIN", "OWNER");




                })
                .formLogin(withDefaults())
                .logout(withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
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
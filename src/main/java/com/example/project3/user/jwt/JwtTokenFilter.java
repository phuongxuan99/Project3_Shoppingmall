package com.example.project3.user.jwt;

import com.example.project3.user.entity.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtTokenFilter extends  OncePerRequestFilter {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService service;

    public JwtTokenFilter(
            JwtTokenUtils tokenUtils,
            UserDetailsService service
    ) {
        this.tokenUtils = tokenUtils;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. 요청에서 Authorization 헤더를 가져온다.
        String authHeader =
//                 request.getHeader("Authorization");
                request.getHeader(HttpHeaders.AUTHORIZATION);
        // 만약 헤더가 없다면, 인증되지 않은 사용자이다.
        if (authHeader == null) {
            // 다음 필터를 실행하고
            filterChain.doFilter(request, response);
            // 메서드 종료
            return;
            // 예외를 발생시키지 않는 이유는 예외가 발생할 경우,
            // 인증이 필요하지 않은 요청들 도 다 막아버리기 때문
        }

        // 2. 헤더가 Bearer <jwt> 형태인지를 검증한다.
        String[] headerSplit = authHeader.split(" ");
        if (headerSplit.length != 2 || !headerSplit[0].equals("Bearer")) {
            // 다음 필터를 실행하고
            filterChain.doFilter(request, response);
            // 메서드 종료
            return;
        }

        // 3. JWT가 정상인지 확인한다.
        String jwt = headerSplit[1];
        if (!tokenUtils.validate(jwt)) {
            // 다음 필터를 실행하고
            filterChain.doFilter(request, response);
            // 메서드 종료
            return;
        }
        // 여기까지 실행이 되면 JWT는 정상이다.

        // 4. Spring Security에서 인증된 사용자임을 알 수 있도록 설정을 진행한다.
        // 4-1. 인증 정보를 담을 객체를 만든다.
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 4-2. 사용자 정보를 JWT에서 회수한다.
        String username = tokenUtils
                .parseClaims(jwt)
                .getSubject();
        UserDetails userDetails = service.loadUserByUsername(username);
        // 4-3. 사용자 이름을 바탕으로 Authentication을 생성한다.
        AbstractAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
        // 4-4. 인증 정보를 설정한다.
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

}
//    private final JwtTokenUtils jwtTokenUtils;
//    private final CustomUserDetails customUserDetails;
//
//    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils, CustomUserDetails customUserDetails) {
//        this.jwtTokenUtils = jwtTokenUtils;
//        this.customUserDetails = customUserDetails;
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        // "Authorization" 헤더에서 JWT 추출
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7); // "Bearer " 제거
//            username = jwtTokenUtils.extractUsername(token);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // JWT 유효성 검사
//            if (jwtTokenUtils.validateToken(token, username)) {
//                UserDetails userDetails = customUserDetails.loadUserByUsername(username);
//                // 인증 객체 생성 및 SecurityContext에 설정
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//
//        filterChain.doFilter(request, response); // 필터 체인 계속 진행
//    }
//}

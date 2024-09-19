package com.example.project3.user.jwt;

import com.example.project3.user.entity.CustomUserDetails;
import com.example.project3.user.jwt.dto.JwtRequestDto;
import com.example.project3.user.jwt.dto.JwtResponseDto;
import com.example.project3.user.request.LoginRequest;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
@RestController
@RequestMapping("token")
public class JwtController {
    private final JwtTokenUtils tokenUtils;
    // 1. 사용자 정보를 조회하는 방법
    private final UserDetailsService userService;
    // 2. 비밀번호를 대조하는 방법
    private final PasswordEncoder passwordEncoder;

    public JwtController(JwtTokenUtils tokenUtils, UserDetailsService userService, PasswordEncoder passwordEncoder) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/issue")
    public JwtResponseDto issueJwt(
            @RequestBody
            JwtRequestDto dto
    ) {
        UserDetails userDetails;
        // 사용자 정보를 조회하자.
        try {
            userDetails = userService.loadUserByUsername(dto.getUsername());
        } catch (UsernameNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 비밀번호를 대조하자.
        if (!passwordEncoder.matches(
                dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // JWT 발급
        String jwt = tokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }


    @GetMapping("/validate-test")
    public String validateTest(
            @RequestParam("token")
            String token
    ) {
        if (!tokenUtils.validate(token))
            return "not valid jwt";
        return "valid jwt";
    }

    @GetMapping("/validate")
    public Claims validate(
            @RequestHeader(value = "Authorization", required = false)
            // Bearer <jwt 문자열>
            String authHeader
    ) {
        if (authHeader == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String[] headerSplit = authHeader.split(" ");
        // 잘못된 헤더 값이 입력되었을 때
        if (headerSplit.length != 2 || !headerSplit[0].equals("Bearer"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // headerSplit = { "Bearer", "jwt" };
        // JWT가 잘못되었을 때
        String jwt = headerSplit[1];
        if (!tokenUtils.validate(jwt))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return tokenUtils.parseClaims(jwt);
    }

    @GetMapping("is-authenticated")
    public String isAuthenticated() {
        return "Success";
    }

}
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenUtils jwtTokenUtils;
//    private final CustomUserDetails customUserDetails;
//
//    public JwtController(AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils, CustomUserDetails customUserDetails) {
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenUtils = jwtTokenUtils;
//        this.customUserDetails = customUserDetails;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestBody LoginRequest request
//    ) {
//
//        try {
//            // AuthenticationManager를 사용해 아이디와 비밀번호 인증
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//        } catch (BadCredentialsException e) {
//            // 인증 실패 시 예외 처리
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
//        }
//
//        // 인증 성공 시, JWT 발급
//        final UserDetails userDetails = customUserDetails.loadUserByUsername(request.getUsername());
//        final String jwt = jwtTokenUtils.generateToken(userDetails.getUsername());
//
//        return ResponseEntity.ok(new JwtResponseDto(jwt));
//    }
//
//
//}

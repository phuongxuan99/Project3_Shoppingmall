package com.example.project3.user.jwt;


import com.example.project3.user.entity.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SignatureException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;


@Slf4j
@Component
public class JwtTokenUtils {
    // JWT 암호키를 담기 위한 객체
    private final Key secretKey;
    // JWT를 해석하는 객체
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        log.info("jwtSecret: {}", jwtSecret);
        this.secretKey =
                Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.secretKey)
                .build();
    }

    public String generateToken(UserDetails userDetails) {
        // 현재시각을 기준으로 만료 시각을 정해야 함으로, 현재 시각을 구한다.
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                // 이 JWT를 들고있는 사람이 누구인지
                .setSubject(userDetails.getUsername())
                // 이 JWT가 언제 생성되었는지
                .setIssuedAt(Date.from(now))
                // 이 JWT가 언제 만료되는지
//                .setExpiration(Date.from(now.plusSeconds(60 * 60 * 24)));
                .setExpiration(Date.from(now.plusSeconds(60 * 60)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.secretKey)
                .compact();
    }


    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired token");
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    public Claims parseClaims(String token) {
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }

}
//    private String secret = "javatechie";
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, username);
//    }
//
//    private String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24))
//                .compact();
//    }
//
//    public Boolean validateToken(String token, String username) {
//        final String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }
//
//}
//

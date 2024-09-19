package com.example.project3.user.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
//    public CharSequence getPassword() {
//        return password;
//    }


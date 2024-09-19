package com.example.project3.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String nickname;
    private String name;

    private Integer ageGroup;
    private String email;
    private String phoneNumber;

    private MultipartFile profileImage;


}


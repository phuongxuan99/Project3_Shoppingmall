package com.example.project3.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private String name;
    private String ageGroup;
    private String email;
    private String phoneNumber;

    private boolean isBusiness;
    private boolean isActive;



    @Enumerated(EnumType.STRING)
    private UserRole role; // 사용자 역할

    private String profileImageUrl; // 프로필 이미지 URL 필드 추가

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    
    public void setAgeGroup(String ageGroup) {
    }


    public void setProfileImage(byte[] bytes) {

    }
}

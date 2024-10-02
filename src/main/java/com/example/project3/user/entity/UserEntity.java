package com.example.project3.user.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column
    private String username;
    private String password;
    @Setter
    private String nickname;
    @Setter
    private String name;
    @Setter
    private Integer ageGroup;
    @Setter
    @Column(unique = true)
    private String email;
    @Setter
    @Column(unique = true)
    private String phoneNumber;

    @Setter
    private boolean isBusiness;
    @Setter
    private boolean isActive;



//    @Enumerated(EnumType.STRING)
//    private UserRole role; // 사용자 역할

    @Setter
    private String profileImg;
    @Setter
    @Builder.Default
    private String roles = "ROLE_INACTIVE"; // 프로필 이미지 URL 필드 추가

//    // Getters and Setters
//    public String getUsername() {
//        return username;
//    }
//
//
//    public void setAgeGroup(String ageGroup) {
//    }
//
//
//    public void setProfileImage(byte[] bytes) {


}

package com.example.project3.user.dto;

import com.example.project3.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private String name;
    private Integer age;
    private String email;
    private String phone;
    private String profileImg;
    private List<String> roles;

    public static UserDto fromEntity(UserEntity entity) {
        List<String> roles = Arrays.stream(entity.getRoles().split(","))
                .toList();

        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .age(entity.getAgeGroup())
                .email(entity.getEmail())
                .phone(entity.getPhoneNumber())
                .profileImg(entity.getProfileImg())
                .roles(roles)
                .build();
    }
}

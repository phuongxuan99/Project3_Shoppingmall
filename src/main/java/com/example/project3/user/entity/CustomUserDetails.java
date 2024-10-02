package com.example.project3.user.entity;

import com.example.project3.user.repo.UserRepository;

import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Integer ageGroup;
    private String email;
    private String phoneNumber;
    private String profileImg;
    private String rolesRaw;
    @Getter
    private UserEntity entity;

    public static CustomUserDetails fromEntity(UserEntity entity) {
        return CustomUserDetails.builder()
                .entity(entity)
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(entity.getRoles().split(","))
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }

    @Override
    public String getPassword() {
        return this.entity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.entity.getUsername();
    }

    public Long getId() {
        return this.entity.getId();
    }

    public Integer getAge() {
        return this.entity.getAgeGroup();
    }

    public String getEmail() {
        return this.entity.getEmail();
    }

    public String getPhone() {
        return this.entity.getPhoneNumber();
    }

    public String getProfileImg() {
        return this.entity.getProfileImg();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
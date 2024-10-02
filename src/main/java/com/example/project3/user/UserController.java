package com.example.project3.user;

import com.example.project3.user.dto.CreateUserDto;
import com.example.project3.user.dto.FileStorageService;

import com.example.project3.user.dto.UpdateUserDto;
import com.example.project3.user.dto.UserDto;
import com.example.project3.user.entity.UserEntity;
import com.example.project3.user.jwt.dto.JwtRequestDto;
import com.example.project3.user.jwt.dto.JwtResponseDto;
import com.example.project3.user.repo.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("signin")
    public JwtResponseDto signIn(
            @RequestBody
            JwtRequestDto dto
    ) {
        return userService.signin(dto);
    }

    @PostMapping("signup")
    public UserDto signUp(
            @RequestBody
            CreateUserDto dto
    ) {
        return userService.createUser(dto);
    }

    @PutMapping("details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto signUpFinal(
            @RequestBody
            UpdateUserDto dto
    ) {
        return userService.updateUser(dto);
    }

    @PutMapping("upgrade")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upgrade() {
        userService.upgradeRoleRequest();
    }

    @PutMapping(
            value = "profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UserDto profileImg(
            @RequestParam("file")
            MultipartFile file
    ) {
        return userService.profileImg(file);
    }

    @GetMapping("get-user-info")
    public UserDto getUserInfo() {
        return userService.getUserInfo();
    }
}
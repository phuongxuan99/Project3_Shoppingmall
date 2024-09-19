package com.example.project3.user;

import com.example.project3.user.dto.FileStorageService;

import com.example.project3.user.entity.UserEntity;
import com.example.project3.user.repo.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@Table(name = "user_id")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, UserService userService, FileStorageService fileStorageService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "users/login-form";
    }

    @GetMapping("/register")
    public String signUpForm() {
        return "users/register-form";
    }

    @PostMapping("/users/register")
    public String registerUser(
            @RequestParam
            String username,
            @RequestParam
            String password,
            @RequestParam
            String nickname,
            @RequestParam
            String name,
            @RequestParam
            String ageGroup,
            @RequestParam
            String email,
            @RequestParam
            String phone,
            @RequestParam
            MultipartFile profileImage
    ) throws IOException {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setName(name);
        user.setAgeGroup(ageGroup);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        try {
            user.setProfileImage(profileImage.getBytes());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        userService.registerUser(user);
        return "redirect:/users/login";
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateUserInfo(@RequestBody UserEntity userEntity) {
        userService.updateUserInfo(userEntity);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/apply-business")
    public ResponseEntity<Void> applyForBusinessUser(@RequestBody UserEntity userEntity) {
        userService.applyForBusinessUser(userEntity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/business-requests")
    public ResponseEntity<List<UserEntity>> getBusinessUserRequests() {
        return ResponseEntity.ok(userService.getBusinessUserRequests());
    }

    @PostMapping("/approve-business/{userId}")
    public ResponseEntity<Void> approveBusinessUser(@PathVariable Long userId) {
        userService.approveBusinessUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reject-business/{userId}")
    public ResponseEntity<Void> rejectBusinessUser(@PathVariable Long userId) {
        userService.rejectBusinessUser(userId);
        return ResponseEntity.noContent().build();
    }
}
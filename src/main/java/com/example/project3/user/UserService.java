package com.example.project3.user;

import com.example.project3.user.entity.UserEntity;
import com.example.project3.user.entity.UserRole;
import com.example.project3.user.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }



    @Transactional
    public UserEntity registerUser(UserEntity userEntity) {
        // 기본 비즈니스 로직 (예: 비활성 사용자로 등록)
        userEntity.setRole(UserRole.INACTIVE_USER);
        return userRepository.save(userEntity);
    }

    @Transactional
    public void updateUserInfo(UserEntity userEntity) {
        // 필수 정보 추가 시 일반 사용자로 전환
        userEntity.setRole(UserRole.REGULAR_USER);
        userRepository.save(userEntity);
    }

    @Transactional
    public void applyForBusinessUser(UserEntity userEntity) {
        // 사업자 사용자 전환 신청
        if (userEntity.getRole() == UserRole.REGULAR_USER) {
            userEntity.setRole(UserRole.BUSINESS_USER);
            userRepository.save(userEntity);
        }
    }

    public List<UserEntity> getBusinessUserRequests() {
        // 사업자 사용자 전환 신청 목록 조회
        return userRepository.findAll(); // 실제로는 필터링 로직 필요
    }

    @Transactional
    public void approveBusinessUser(Long userId) {
        // 사업자 사용자 전환 승인
        UserEntity user = userRepository.findById(userId).orElseThrow();
        user.setRole(UserRole.BUSINESS_USER);
        userRepository.save(user);
    }

    @Transactional
    public void rejectBusinessUser(Long userId) {
        // 사업자 사용자 전환 거부
        UserEntity user = userRepository.findById(userId).orElseThrow();
        user.setRole(UserRole.REGULAR_USER);
        userRepository.save(user);
    }
}






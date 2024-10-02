package com.example.project3.user;

import com.example.project3.FileHandlerUtils;
import com.example.project3.user.dto.CreateUserDto;
import com.example.project3.user.dto.UpdateUserDto;
import com.example.project3.user.dto.UserDto;
import com.example.project3.user.entity.CustomUserDetails;
import com.example.project3.user.entity.UserEntity;
import com.example.project3.user.entity.UserUpgrade;
import com.example.project3.user.jwt.JwtTokenUtils;
import com.example.project3.user.jwt.dto.JwtRequestDto;
import com.example.project3.user.jwt.dto.JwtResponseDto;
import com.example.project3.user.repo.UserRepository;
import com.example.project3.user.repo.UserUpgradeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserService implements UserDetailsService {

    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;
    private final UserUpgradeRepo userUpgradeRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final FileHandlerUtils fileHandlerUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
    }

    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return UserDto.fromEntity(userRepository.save(UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build()));
    }

    public JwtResponseDto signin(JwtRequestDto dto) {
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(
                dto.getPassword(),
                userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        String jwt = jwtTokenUtils.generateToken(CustomUserDetails.fromEntity(userEntity));
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }

    public UserDto updateUser(UpdateUserDto dto){
        UserEntity userEntity = authFacade.extractUser();
        userEntity.setNickname(dto.getNickname());
        userEntity.setName(dto.getName());
        userEntity.setAgeGroup(dto.getAge());
        userEntity.setPhoneNumber(dto.getPhone());
        userEntity.setEmail(dto.getEmail());
        if (
                userEntity.getNickname() != null &&
                        userEntity.getName() != null &&
                        userEntity.getAgeGroup() != null &&
                        userEntity.getEmail() != null &&
                        userEntity.getPhoneNumber() != null &&
                        userEntity.getRoles().equals("ROLE_INACTIVE")
        )
            userEntity.setRoles("ROLE_ACTIVE");
        return UserDto.fromEntity(userRepository.save(userEntity));
    }

    public void upgradeRoleRequest() {
        UserEntity target = authFacade.extractUser();
        if (target.getRoles().contains("ROLE_OWNER"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        userUpgradeRepo.save(UserUpgrade.builder()
                .target(target)
                .build()
        );
    }

    public UserDto profileImg(MultipartFile file) {
        UserEntity userEntity = authFacade.extractUser();
        String requestPath = fileHandlerUtils.saveFile(
                String.format("users/%d/", userEntity.getId()),
                "profile",
                file
        );

        userEntity.setProfileImg(requestPath);
        return UserDto.fromEntity(userRepository.save(userEntity));
    }

    public UserDto getUserInfo() {
        return UserDto.fromEntity(authFacade.extractUser());
    }
}
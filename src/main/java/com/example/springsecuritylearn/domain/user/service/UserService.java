package com.example.springsecuritylearn.domain.user.service;

import com.example.springsecuritylearn.domain.user.dto.UserRequestDTO;
import com.example.springsecuritylearn.domain.user.entity.UserEntity;
import com.example.springsecuritylearn.domain.user.entity.UserRole;
import com.example.springsecuritylearn.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void join(UserRequestDTO dto) {

        String username = dto.getUsername();
        String password = dto.getPassword();

        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setRole(UserRole.USER);

        userRepository.save(entity);
    }
}

package com.example.springsecuritylearn.domain.user.service;

import com.example.springsecuritylearn.domain.user.dto.UserRequestDTO;
import com.example.springsecuritylearn.domain.user.entity.UserEntity;
import com.example.springsecuritylearn.domain.user.entity.UserRole;
import com.example.springsecuritylearn.domain.user.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

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

    // 유저 유무 확인
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        UserEntity entity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("not found"));

        return User.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(entity.getRole().name())
                .build();
    }

    /*
    // 권한 검사 메소드
    public boolean authCheck(Long postId) {
1
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // 커스텀 쿼리
        return postRepository.existsIdAndUsernameCustom(postId, username);
    }
    */
}

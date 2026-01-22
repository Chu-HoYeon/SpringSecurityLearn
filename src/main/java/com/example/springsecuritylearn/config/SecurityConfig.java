package com.example.springsecuritylearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final String rememberMeKey;

    public SecurityConfig(@Value("${spring.security.remember-me.key}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    // 비밀번호 암호화 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Role Hierarchy 설정
    @Bean
    public RoleHierarchy roleHierarchy(){

        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role("ADMIN").implies("USER")
                .build();
    }

    // 시큐리티 필터 커스텀
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        // CSRF 필터 활성화. CSRF 설정시 로그아웃은 무조건 POST 요청만 허용되기 때문에 설정을 추가
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/logout")
                );
        // 로그인 필터 설정
        http
                .formLogin(login -> login
                        .loginProcessingUrl("/login")
                        .loginPage("/login")
                );

        // Remember-Me 설정
        http
                .rememberMe(me -> me
                        .key(rememberMeKey)
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(14 * 24 * 60 * 60)
                );

        // 인가 필터 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/join").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user/").hasAnyRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().denyAll()
                );

        // 최종 필터 빌드
        return http.build();
    }

}

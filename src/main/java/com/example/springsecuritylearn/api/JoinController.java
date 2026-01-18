package com.example.springsecuritylearn.api;

import com.example.springsecuritylearn.domain.user.dto.UserRequestDTO;
import com.example.springsecuritylearn.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(){
        return "join";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(UserRequestDTO dto){
        userService.join(dto);
        return "redirect:/";
    }
}

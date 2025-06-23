package com.example.boardHub.user.controller;

import com.example.boardHub.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserViewController {

    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login"; // templates/user/register.html을 렌더링
    }

}

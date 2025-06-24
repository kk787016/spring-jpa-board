package com.example.boardHub.user.controller;

import com.example.boardHub.user.dto.UserRequestDto;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController{

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto userDto) {
        try {
            userService.registerUser(userDto.getUserId(), userDto.getPassword(), userDto.getUsername() , userDto.getNickname());
            return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}

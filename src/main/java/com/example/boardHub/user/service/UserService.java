package com.example.boardHub.user.service;

import com.example.boardHub.user.model.User;
import com.example.boardHub.user.repository.UserRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void registerUser(String userId, String password, String username ,String nickname) {
        if (userRepository.existsByUserId(userId)) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        // 새로운 사용자 저장
        User newUser = User.createUser(userId, encodedPassword, username, nickname);// createdDate 자동 설정됨
        userRepository.save(newUser);
    }


}

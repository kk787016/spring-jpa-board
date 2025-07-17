package com.example.boardHub.user.service;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.global.exception.BoardNotFoundException;
import com.example.boardHub.global.exception.UserNotFoundException;
import com.example.boardHub.user.dto.UserRequestDto;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.repository.UserRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void registerUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByUserId(userRequestDto.getUserId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickname(userRequestDto.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        // 새로운 사용자 저장
        User newUser = User.builder()
                .userId(userRequestDto.getUserId())
                .password(encodedPassword)
                .username(userRequestDto.getUsername())
                .nickname(userRequestDto.getNickname())
                .build();


        userRepository.save(newUser);
    }


    public User getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다: " + userId));
    }

}

package com.example.boardHub.user.controller;

import com.example.boardHub.global.config.jwt.JwtTokenProvider;
//import com.example.boardHub.global.context.UserContext;
import com.example.boardHub.user.dto.LoginRequestDto;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.model.UserDetailsImpl;
import com.example.boardHub.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserId(), request.getPassword()
                    )
            );

            String userId = authentication.getName();

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

            String accessToken = jwtTokenProvider.createAccessToken(user);
            String refreshToken = jwtTokenProvider.createRefreshToken(user);


            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false) // HTTPS에서 true로 변경
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            // ✅ Access Token은 JSON 응답으로 전달 (localStorage에 저장하게)
            return ResponseEntity.ok().body(Map.of("accessToken", accessToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "아이디 또는 비밀번호가 잘못되었습니다."));
        }

    }
}
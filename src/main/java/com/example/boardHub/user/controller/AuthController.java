package com.example.boardHub.user.controller;

import com.example.boardHub.global.config.jwt.JwtTokenProvider;
import com.example.boardHub.user.dto.LoginRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserId(), request.getPassword()
                    )
            );

            String accessToken = jwtTokenProvider.createAccessToken(authentication.getName());
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication.getName());

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false) // 로컬에서 테스트 시 false 가능
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7일
                    .sameSite("Strict") // 또는 "Lax" / "None"
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());




            return ResponseEntity.ok().body(Map.of(
                    "accessToken", accessToken
                    , "refreshToken", refreshToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "아이디 또는 비밀번호가 잘못되었습니다."));
        }

    }
}
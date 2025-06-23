package com.example.boardHub.user.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String userId;
    private String password;
    private String nickname;
    private String username;
}

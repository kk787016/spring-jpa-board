package com.example.boardHub.user.model;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import com.example.boardHub.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "nickname", nullable = false,unique = true)
    private String nickname;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)  // 게시판 지워져도 유저는 있어야 되니까. casecade X
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;


    @Builder
    public User(String userId, String password, String username, String nickname){
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = UserRoleEnum.USER;

    }
}

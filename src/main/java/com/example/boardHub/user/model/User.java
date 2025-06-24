package com.example.boardHub.user.model;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false,unique = true)
    private String nickname;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)  // 게시판 지워져도 유저는 있어야 되니까. casecade X
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String userId, String password, String username, String nickname){
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.nickname = nickname;

    }
}

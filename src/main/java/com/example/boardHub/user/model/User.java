package com.example.boardHub.user.model;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
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

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "user")  // 게시판 지워져도 유저는 있어야 되니까. casecade X
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public static User createUser(String userId, String password, String username, String nickname){
        User user = new User();
        user.userId = userId;
        user.password = password;
        user.username = username;
        user.nickname = nickname;
        user.createdDate = LocalDateTime.now();
        return user;
    }
}

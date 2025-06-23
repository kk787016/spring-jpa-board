package com.example.boardHub.board.model;

import com.example.boardHub.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Board parent;

    @OneToMany(mappedBy = "parent")
    private List<Board> children = new ArrayList<>();

    private int viewCount;


    private boolean deleted = false; // Soft delete 용도

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}

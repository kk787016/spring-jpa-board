package com.example.boardHub.board.model;

import com.example.boardHub.global.config.BaseEntity;
import com.example.boardHub.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Board parent;

    @OneToMany(mappedBy = "parent")
    private List<Board> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false; // Soft delete 용도

    private Long viewCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "dislike_count", nullable = false)
    private int disLikeCount = 0;

    @Builder
    public Board(String title, String content, User user, Board parent) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.parent = parent;
        this.viewCount = 0L;
        this.deleted = false;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }


    public void updateViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    // Soft Delete 처리 메서드
    public void softDelete() {
        this.deleted = true;
    }

    public void addChildBoard(Board childBoard) {
        this.children.add(childBoard);
        childBoard.setParent(this); // 자식 Board 엔티티에도 부모 Board 설정
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setBoard(this); // Comment 엔티티에도 Board 설정
    }
    public void updateLikeCount(int likeCount, int disLikeCount) {
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;

    }
    public boolean emptyChild() {
        return this.getChildren().isEmpty();

    }
}

package com.example.boardHub.board.model;

import com.example.boardHub.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @Setter
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false; // Soft delete 용도

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Comment(String content, Board board, User user, Comment parent) {
        this.content = content;
        this.board = board;
        this.user = user;
        this.parent = parent;
        this.deleted = false;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    // Soft Delete 처리 메서드
    public void softDelete() {
        this.deleted = true;
    }

    public void addChildComment(Comment childComment) {
        this.children.add(childComment);
        childComment.setParent(this);
    }

    public boolean isLastChild() {
        return this.parent.isDeleted() && this.getChildren().size() == 1;
    }

}

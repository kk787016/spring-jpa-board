package com.example.boardHub.board.model;

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
@Table(name="boards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Board parent;

    @OneToMany(mappedBy = "parent")
    private List<Board> children = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted = false; // Soft delete 용도
    private Long viewCount;

    @Builder
    public Board (String title,String content, User user, Board parent){
        this.title = title;
        this.content = content;
        this.user = user;
        this.parent=parent;
        this.viewCount = 0L;
        this.deleted = false;
    }

    public void update(String title, String content) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
        }
    }


    public void incrementViewCount() {
        this.viewCount++;
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

}

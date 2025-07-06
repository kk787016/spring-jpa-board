package com.example.boardHub.board.dto.response;

import com.example.boardHub.board.model.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private boolean deleted;
    private List<CommentResponseDto> children;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.deleted = comment.isDeleted();
        this.createdAt = comment.getCreatedAt();
        if (this.deleted) {
            this.nickname = "삭제된 사용자";
            this.content = "삭제된 댓글입니다.";
        }
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.children = comment.getChildren().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}

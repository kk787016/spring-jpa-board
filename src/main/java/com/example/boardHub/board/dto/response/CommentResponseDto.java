package com.example.boardHub.board.dto.response;

import com.example.boardHub.board.model.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final String nickname;
    private final LocalDateTime createdAt;
    private final boolean deleted;
    private final List<CommentResponseDto> children = new ArrayList<>();

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent();
        this.deleted = comment.isDeleted();
        this.createdAt = comment.getCreatedAt();
        this.nickname = comment.getUser().getNickname();
    }
    public void addChild(CommentResponseDto child) {
        this.children.add(child);
    }
}

package com.example.boardHub.board.dto.response;

import com.example.boardHub.board.model.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private List<CommentResponseDto> comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long totalViews;

    private Long userId;
    private String nickname;

    private boolean deleted;

    public BoardResponseDto(Board board, Long newViewCount) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.totalViews = newViewCount;
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.deleted = board.isDeleted();
        this.comments = board.getComments().stream()
                .filter(comment -> comment.getParent() == null)
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}

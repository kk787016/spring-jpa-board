package com.example.boardHub.board.dto.response;

import com.example.boardHub.board.model.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final List<CommentResponseDto> comments;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final Long totalViews;

    private final Long userId;
    private final String nickname;

    private final boolean deleted;

    private final long likeCount;
    private final long dislikeCount;
    private final boolean userLiked;
    private final boolean userDisLiked;

    public BoardResponseDto(Board board,
                            Long newViewCount,
                            RecommendationResponseDto r,
                            List<CommentResponseDto> commentResponses) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.totalViews = newViewCount;
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.deleted = board.isDeleted();
        this.likeCount = r.getLikeCount();
        this.dislikeCount = r.getDislikeCount();
        this.userLiked = r.isUserLiked();
        this.userDisLiked = r.isUserDisLiked();
        this.comments = commentResponses != null ? commentResponses : new ArrayList<>();

    }
}

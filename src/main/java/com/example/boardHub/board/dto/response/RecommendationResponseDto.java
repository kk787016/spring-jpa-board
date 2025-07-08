package com.example.boardHub.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationResponseDto {

    private Long likeCount;
    private Long dislikeCount;
    private boolean userLiked;
    private boolean userDisLiked;

}

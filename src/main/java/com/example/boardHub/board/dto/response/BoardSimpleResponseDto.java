package com.example.boardHub.board.dto.response;


import com.example.boardHub.board.model.Board;
import lombok.Getter;

@Getter
public class BoardSimpleResponseDto {

    private final Long id;
    private final String title;
    private final String nickname;
    private final int likeCount;

    public BoardSimpleResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.nickname = board.getUser().getNickname();
        this.likeCount = board.getLikeCount();
    }
}

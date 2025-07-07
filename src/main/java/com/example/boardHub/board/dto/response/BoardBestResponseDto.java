package com.example.boardHub.board.dto.response;

import com.example.boardHub.board.model.Board;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardBestResponseDto {

    private List<BoardSimpleResponseDto> boards;

    public BoardBestResponseDto(List<BoardSimpleResponseDto> boards) {
        this.boards = boards;
    }
}

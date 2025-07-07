package com.example.boardHub.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BOARD_NOT_FOUND("B404", "요청한 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND("C404", "요청한 댓글을 찾을 수 없습니다."),
    INVALID_ARGUMENT("A400", "잘못된 접근입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}

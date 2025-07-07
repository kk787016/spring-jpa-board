package com.example.boardHub.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity <ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 인자 값 또는 권한 없음: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_ARGUMENT.getCode(), ErrorCode.INVALID_ARGUMENT.getMessage()));

    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoardNotFoundException(BoardNotFoundException e) {
        log.warn("게시글 존재하지 않음: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 상태 코드 반환
                .body(new ErrorResponse(ErrorCode.BOARD_NOT_FOUND.getCode(), ErrorCode.BOARD_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        log.warn("댓글 존재하지 않음: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.COMMENT_NOT_FOUND.getCode(), ErrorCode.COMMENT_NOT_FOUND.getMessage()));
    }
}

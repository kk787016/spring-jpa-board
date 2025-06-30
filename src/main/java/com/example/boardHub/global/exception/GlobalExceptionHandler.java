package com.example.boardHub.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        log.warn("잘못된 인자 값 또는 권한 없음: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "잘못된 접근 , 권한 없습니다.");
        return "redirect:/";
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<String> handleBoardNotFoundException(BoardNotFoundException e) {
        log.warn("게시글 존재하지 않음: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 상태 코드 반환
                .body("요청한 게시글을 찾을 수 없습니다.");
    }
}

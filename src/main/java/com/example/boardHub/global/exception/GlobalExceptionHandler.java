package com.example.boardHub.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
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
    public String handleBoardNotFoundException(BoardNotFoundException ex, RedirectAttributes redirectAttributes) {
        log.warn("게시글 존재하지 않음: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "찾으시는 게시글이 존재하지 않거나 삭제되었습니다.");
        return "redirect:/";
    }
}

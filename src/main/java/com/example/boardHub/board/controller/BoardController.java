package com.example.boardHub.board.controller;

import com.example.boardHub.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/new")
    public String createForm() {
        return "board/create";
    }

    @GetMapping("/{boardId}")
    public String showForm() {
        return "board/detail";
    }

}

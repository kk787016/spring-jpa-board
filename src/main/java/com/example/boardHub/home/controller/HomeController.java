package com.example.boardHub.home.controller;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/")
    public String homeView(Model model) {

        List<Board> boards = boardService.getAllBoards();

        model.addAttribute("boards", boards);
        return "list";
    }

}

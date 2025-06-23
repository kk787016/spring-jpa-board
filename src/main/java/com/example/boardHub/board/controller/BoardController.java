package com.example.boardHub.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    // @Autowired
    // private BoardService boardService;

    @GetMapping("/")
    public String list() {
        return "list";
    }

    @GetMapping("/new")
    public String createForm() {
        return "board/create";
    }

    @GetMapping("/{id}")
    public String detail() {
        return "board/detail";
    }

}
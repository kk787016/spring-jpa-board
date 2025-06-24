package com.example.boardHub.board.controller;


//import com.example.boardHub.global.context.UserContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {


    @GetMapping("/new")
    public String createForm() {
        return "board/create";
    }

    @GetMapping("/{id}")
    public String detail() {
        return "board/detail";
    }



//    @PostMapping("/new")
//    public ResponseEntity<?> createBoard() {
//        String userId = UserContext.getUserId();
//        if (userId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//        //boardService.createPost(userId, dto);
//        return ResponseEntity.ok().build();
//    }
}
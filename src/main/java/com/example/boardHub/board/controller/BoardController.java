package com.example.boardHub.board.controller;

import com.example.boardHub.board.dto.BoardRequestDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.service.BoardService;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.model.UserDetailsImpl;
import com.example.boardHub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Objects;

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
    public String detail(@PathVariable Long boardId, Model model) {

        Board board = boardService.getBoardDetail(boardId);
        model.addAttribute("board", board);

        return "board/detail";
    }

    @PostMapping("/new")
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.registerBoard(boardRequestDto, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 성공"));

    }

    @DeleteMapping("/delete/{deleteBoardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long deleteBoardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.deleteBoard(deleteBoardId, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 삭제 성공"));

    }

    @PatchMapping("/update/{updateBoardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long updateBoardId,
                                         @RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.updateBoard(updateBoardId, boardRequestDto, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 수정 성공"));


    }

    @PostMapping("/{parentBoardId}/new")
    public ResponseEntity<?> createReplyBoard(@PathVariable Long parentBoardId,
                                              @RequestBody BoardRequestDto boardRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.registerReplyBoard(parentBoardId, boardRequestDto, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 답글 성공"));
    }
}

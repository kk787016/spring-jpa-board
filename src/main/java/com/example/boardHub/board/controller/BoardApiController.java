package com.example.boardHub.board.controller;

import com.example.boardHub.board.dto.request.BoardRequestDto;
import com.example.boardHub.board.dto.response.BoardBestResponseDto;
import com.example.boardHub.board.dto.response.BoardResponseDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.service.BoardService;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("/new")
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.registerBoard(boardRequestDto, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 성공"));

    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> detail(@PathVariable Long boardId) {

        Board board = boardService.getBoardDetail(boardId);
        long totalViews = boardService.getTotalViews(board);

        BoardResponseDto responseBoard = new BoardResponseDto(board,totalViews);

        return ResponseEntity.ok(responseBoard);
    }
    @GetMapping("/best")
    public ResponseEntity<BoardBestResponseDto> bestBoard() {
        BoardBestResponseDto response = boardService.getBestBoards();
        return ResponseEntity.ok(response);
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

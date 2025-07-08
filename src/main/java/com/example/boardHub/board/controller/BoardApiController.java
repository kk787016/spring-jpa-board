package com.example.boardHub.board.controller;

import com.example.boardHub.board.dto.request.BoardRequestDto;
import com.example.boardHub.board.dto.response.BoardBestResponseDto;
import com.example.boardHub.board.dto.response.BoardResponseDto;
import com.example.boardHub.board.dto.response.CommentResponseDto;
import com.example.boardHub.board.dto.response.RecommendationResponseDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import com.example.boardHub.board.repository.CommentRepository;
import com.example.boardHub.board.service.BoardService;
import com.example.boardHub.board.service.CommentService;
import com.example.boardHub.board.service.RecommendationService;
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
    private final RecommendationService recommendationService;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto boardRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.registerBoard(boardRequestDto, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 성공"));

    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> detail(@PathVariable Long boardId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails != null ? userDetails.getUser().getId() : null;

        Board board = boardService.getBoardDetail(boardId);
        long totalViews = boardService.getTotalViews(board);

        List<Comment> comments = commentRepository.findAllByBoardIdWithUser(boardId);

        RecommendationResponseDto recommendResult = recommendationService.getRecommendationStatus(boardId, userId);
        List<CommentResponseDto> commentResponses = commentService.buildTwoLevelCommentTree(comments);

        BoardResponseDto responseBoard = new BoardResponseDto(board,totalViews,recommendResult, commentResponses);

        return ResponseEntity.ok(responseBoard);
    }
    @GetMapping("/best")
    public ResponseEntity<BoardBestResponseDto> bestBoard() {
        BoardBestResponseDto response = boardService.getBestBoards();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{deleteBoardId}")
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

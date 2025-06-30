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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    @GetMapping("/new")
    public String createForm() {
        return "board/create";
    }

    @GetMapping("/{boardId}")
    public String detail(@PathVariable Long boardId, Model model, Authentication authentication) {

        Board board = boardService.getBoardDetail(boardId);
        model.addAttribute("board", board);

        boolean hasPermission = false; // 기본값은 권한 없음

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String currentLoggedInUserIdentifier = authentication.getName(); // 현재 로그인한 사용자 식별자 (userId 또는 username)

            if (board.getUser() != null && board.getUser().getUserId() != null) {
                // board.user.userId는 Long 타입일 가능성이 높으므로, toString()으로 문자열 변환하여 비교
                if (currentLoggedInUserIdentifier.equals(board.getUser().getUserId())) {
                    hasPermission = true; // 로그인한 사용자와 작성자 ID가 같으면 권한 있음
                }
            }
        }
        model.addAttribute("hasPermission", hasPermission); // 권한 여부를 모델에 추가

        return "board/detail";
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody BoardRequestDto boardRequestDto,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.registerBoard(
                boardRequestDto.getTitle(),
                boardRequestDto.getContent(),
                user
        );

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
    public ResponseEntity <?> updateBoard(@PathVariable Long updateBoardId,
                                          @RequestBody BoardRequestDto boardRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        boardService.updateBoard(updateBoardId,boardRequestDto, user);

        return ResponseEntity.ok().body(Map.of("message", "게시판 수정 성공"));

    }
}
package com.example.boardHub.board.controller;


//import com.example.boardHub.global.context.UserContext;
import com.example.boardHub.board.dto.BoardRequestDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.service.BoardService;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.repository.UserRepository;
import com.example.boardHub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Board board = boardService.getBoardDetail(id);

        model.addAttribute("board", board);
        boolean hasPermission = false; // 기본값은 권한 없음

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String currentLoggedInUserIdentifier = authentication.getName(); // 현재 로그인한 사용자 식별자 (userId 또는 username)

            if (board.getUser() != null && board.getUser().getUserId() != null) {
                // board.user.userId는 Long 타입일 가능성이 높으므로, toString()으로 문자열 변환하여 비교
                if (currentLoggedInUserIdentifier.equals(board.getUser().getUserId().toString())) {
                    hasPermission = true; // 로그인한 사용자와 작성자 ID가 같으면 권한 있음
                }
            }
        }
        model.addAttribute("hasPermission", hasPermission); // 권한 여부를 모델에 추가

        return "board/detail";
    }
    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody BoardRequestDto boardRequestDto, Authentication authentication) {
        try {
            String userId = authentication.getName();
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. userId: " + userId));
            boardService.registerBoard(boardRequestDto.getTitle(),boardRequestDto.getContent(),user);
            return ResponseEntity.ok().body(Map.of("message", "게시판 성공"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
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
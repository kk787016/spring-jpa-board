package com.example.boardHub.board.controller;

import com.example.boardHub.board.dto.request.RecommendationRequestDto;
import com.example.boardHub.board.dto.response.RecommendationResponseDto;
import com.example.boardHub.board.service.RecommendationService;
import com.example.boardHub.user.model.UserDetailsImpl;
import com.example.boardHub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board/{boardId}/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> recommend(@PathVariable Long boardId,
                                       @RequestBody RecommendationRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long userId = userDetails.getUser().getId();

        RecommendationResponseDto result = recommendationService.processRecommendation(boardId, userId, requestDto.getType());

        return ResponseEntity.ok(result);

    }

}


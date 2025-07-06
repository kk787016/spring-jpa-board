package com.example.boardHub.board.controller;

import com.example.boardHub.board.model.RecommendationType;
import com.example.boardHub.board.service.RecommendationService;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.model.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/board/{boardId}/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<?> recommend(@PathVariable Long boardId,
                                       @RequestBody RecommendationRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long userId = userDetails.getUser().getId();

        Map<String, Object> result = recommendationService.processRecommendation(boardId,userId, requestDto.getType());

        return ResponseEntity.ok(result);

    }
}


@Getter
class RecommendationRequestDto {
    private RecommendationType type;
}
package com.example.boardHub.board.controller;


import com.example.boardHub.board.dto.BoardRequestDto;
import com.example.boardHub.board.dto.CommentDto;
import com.example.boardHub.board.model.Comment;
import com.example.boardHub.board.service.CommentService;
import com.example.boardHub.user.model.User;
import com.example.boardHub.user.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long boardId,
                                           @RequestBody CommentDto commentDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        commentService.createComment(boardId, commentDto, user);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 생성 완료");
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long boardId,
                                           @PathVariable Long commentId,
                                           @RequestBody CommentDto commentDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        commentService.updateComment(commentId, commentDto, user);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 완료");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long boardId,
                                           @PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        commentService.deleteComment(commentId, user);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
    }

    @PostMapping("/{parentCommentId}/new")
    public ResponseEntity<?> createReplyComment(@PathVariable Long boardId,
                                                @PathVariable Long parentCommentId,
                                                @RequestBody CommentDto commentDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        commentService.registerReplyComment(boardId, parentCommentId, commentDto, user);

        return ResponseEntity.ok().body(Map.of("message", "댓글 대댓글 성공"));
    }

}

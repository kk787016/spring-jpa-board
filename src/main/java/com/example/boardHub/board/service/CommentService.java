package com.example.boardHub.board.service;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import com.example.boardHub.board.repository.BoardRepository;
import com.example.boardHub.board.repository.CommentRepository;
import com.example.boardHub.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

//    @Transactional
//    public Comment createComment(String comment, Long boardId, User user) {
//        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//
//        //User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. userId: " + userId));
//
//    }

}

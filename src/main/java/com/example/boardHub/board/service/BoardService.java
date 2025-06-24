package com.example.boardHub.board.service;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.repository.BoardRepository;
import com.example.boardHub.user.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board registerBoard(String title, String content, User user) {
        Board newBoard = Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .parent(null)
                .build();

        //save;
        return boardRepository.save(newBoard);
    }

    @Transactional
    public Board registerReplyBoard(String title, String content, User user, Long boardId) {
        Board parentBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("부모 게시글을 찾을 수 없습니다."));

        Board replyBoard = Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .parent(parentBoard)
                .build();
        parentBoard.addChildBoard(replyBoard);
        return boardRepository.save(replyBoard);
    }

   @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        return boardRepository.findByDeletedFalseOrderByCreatedAtDescWithUser();
    }

    // 게시글 상세 조회 및 조회수 증가
    @Transactional // 조회수 증가 및 DB 업데이트를 위해 @Transactional 필요
    public Board getBoardDetail(Long boardId) {

        Board board = boardRepository.findByIdAndDeletedFalseWithUser(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + boardId));

        board.incrementViewCount();

        return boardRepository.save(board);
    }
}

package com.example.boardHub.board.service;

import com.example.boardHub.board.dto.BoardRequestDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.repository.BoardRepository;
import com.example.boardHub.global.exception.BoardNotFoundException;
import com.example.boardHub.user.model.User;

import com.example.boardHub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        return boardRepository.findByDeletedFalseOrderByCreatedAtDescWithUser();
    }


    @Transactional
    public Board registerBoard(String title, String content, User user) {

        Board newBoard = Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .parent(null)
                .build();

        return boardRepository.save(newBoard);
    }

    @Transactional
    public Board registerReplyBoard(String title, String content, User user, Long boardId) {
        Board parentBoard = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("부모 게시글을 찾을 수 없습니다.")
        );

        Board replyBoard = Board.builder()
                .title(title)
                .content(content)
                .user(user)
                .parent(parentBoard)
                .build();
        parentBoard.addChildBoard(replyBoard);

        return boardRepository.save(replyBoard);
    }


    // 게시글 상세 조회 및 조회수 증가
    @Transactional
    public Board getBoardDetail(Long boardId) {

        Board board = boardRepository.findByIdAndDeletedFalseWithUser(boardId).orElseThrow(() ->
                new BoardNotFoundException("게시글을 찾을 수 없습니다: " + boardId)
        );

        board.incrementViewCount();

        return boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = findBoardAndCheckOwnership(boardId, user);
        //boardRepository.delete(board); 전체 삭제 하면 안되고, delete = false
        board.softDelete();

    }

    @Transactional
    public void updateBoard(Long postBoardId, BoardRequestDto requestDto, User user) {
        Board board = findBoardAndCheckOwnership(postBoardId, user);

        if (requestDto.getTitle() != null) {
            board.updateTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            board.updateContent(requestDto.getContent());
        }
        board.updateTime(LocalDateTime.now());
    }

    private Board findBoardAndCheckOwnership(Long boardId, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new BoardNotFoundException("해당 게시글을 찾을 수 없습니다.")
        );

        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 게시글에 대한 권한이 없습니다.");
        }

        return board;
    }
}

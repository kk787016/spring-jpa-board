package com.example.boardHub.board.service;

import com.example.boardHub.board.dto.BoardRequestDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.repository.BoardRepository;
import com.example.boardHub.global.exception.BoardNotFoundException;
import com.example.boardHub.user.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        return boardRepository.findByDeletedFalseOrderByCreatedAtDescWithUser();
    }

    @Transactional
    public void registerBoard(BoardRequestDto boardRequestDto, User user) {

        Board newBoard = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .user(user)
                .parent(null)
                .build();

        boardRepository.save(newBoard);
    }

    // 게시글 상세 조회 및 조회수 증가
    @Transactional
    public Board getBoardDetail(Long boardId) {

        Board board = boardRepository.findByIdAndDeletedFalseWithUser(boardId).orElseThrow(() ->
                new BoardNotFoundException("게시글을 찾을 수 없습니다: " + boardId)
        );

        //board.incrementViewCount();
//        String redisKey = "board:view" + boardId;
//        redisTemplate.opsForValue().increment(redisKey);
//
//        Long redisCount = redisTemplate.opsForValue().get(redisKey);
//        long totalViews = board.getViewCount() + (redisCount != null ? redisCount : 0);

        return board;
        //return new BoardResponseDto(board,totalViews);
        //return boardRepository.save(board);

    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = findBoardAndCheckOwnership(boardId, user);
        Board parent = board.getParent();

        if (parent == null) {
            if (board.getChildren().isEmpty()) {
                boardRepository.delete(board);
            } else {
                board.softDelete();
            }
        } else {
            boolean isLastChild = parent.isDeleted() && parent.getChildren().size() == 1;

            boardRepository.delete(board);
            if (isLastChild) {
                boardRepository.delete(parent);
            }
        }

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
    }

    @Transactional
    public void registerReplyBoard(Long parentBoardId, BoardRequestDto boardRequestDto, User user) {
        Board parentBoard = boardRepository.findById(parentBoardId).orElseThrow(() ->
                new BoardNotFoundException("부모 게시글을 찾을 수 없습니다.")
        );

        Board replyBoard = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .user(user)
                .parent(parentBoard)
                .build();

        parentBoard.addChildBoard(replyBoard);

        boardRepository.save(replyBoard);
    }


    private Board findBoardAndCheckOwnership(Long boardId, User user) {

        Board board = boardRepository.findByIdAndDeletedFalseWithUser(boardId).orElseThrow(() ->
                new BoardNotFoundException("해당 게시글을 찾을 수 없습니다.")
        );

        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 게시글에 대한 권한이 없습니다.");
        }

        return board;
    }

    public long getTotalViews(Board board) {


        String redisKey = "board:view" + board.getId();
        redisTemplate.opsForValue().increment(redisKey);

        Long redisCount = redisTemplate.opsForValue().get("board:view" + board.getId());
        return board.getViewCount() + (redisCount != null ? redisCount : 0);
    }
}

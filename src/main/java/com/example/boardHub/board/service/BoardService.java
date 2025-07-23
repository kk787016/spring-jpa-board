package com.example.boardHub.board.service;

import com.example.boardHub.board.dto.request.BoardRequestDto;
import com.example.boardHub.board.dto.response.BoardBestResponseDto;
import com.example.boardHub.board.dto.response.BoardSimpleResponseDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.repository.BoardRepository;
import com.example.boardHub.global.exception.BoardNotFoundException;
import com.example.boardHub.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final RedisTemplate<String, Long> redisTemplateForViewCount;
    private final RedisTemplate<String, Object> redisTemplateForCaching;

    public BoardService(BoardRepository boardRepository,
                        @Qualifier("redisTemplateForViewCount") RedisTemplate<String, Long> redisTemplateForViewCount,
                        @Qualifier("redisTemplateForCaching")  RedisTemplate<String, Object> redisTemplateForCaching) {
        this.boardRepository = boardRepository;
        this.redisTemplateForViewCount = redisTemplateForViewCount;
        this.redisTemplateForCaching = redisTemplateForCaching;
    }

    private static final String BEST_BOARD_KEY = "best:boards";

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

        return boardRepository.findByIdAndDeletedFalseWithComments(boardId).orElseThrow(() ->
                new BoardNotFoundException("게시글을 찾을 수 없습니다: " + boardId)
        );

    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = findBoardAndCheckOwnership(boardId, user);

        if (board.emptyChild()){
            boardRepository.delete(board);
            return;
        }
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
    //findByIdAndDeletedFalseWithUser
    public Board findBoardForComment(Long boardId) {
        return boardRepository.findByIdAndDeletedFalseWithUser(boardId).orElseThrow(() ->
                new BoardNotFoundException("댓글 작성 중 게시글을 찾을 수 없습니다: " + boardId)
        );
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
        redisTemplateForViewCount.opsForValue().increment(redisKey);

        Long redisCount = redisTemplateForViewCount.opsForValue().get("board:view" + board.getId());
        return board.getViewCount() + (redisCount != null ? redisCount : 0);
    }


    public BoardBestResponseDto getBestBoards() {

        @SuppressWarnings("unchecked")
        List<BoardSimpleResponseDto> cached = (List<BoardSimpleResponseDto>) redisTemplateForCaching.opsForValue().get(BEST_BOARD_KEY);

        if (cached != null) {
            log.info("✅ 캐시 히트 - 베스트 게시글");
            return new BoardBestResponseDto(cached);
        }

        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
        List<Board> bestBoards = boardRepository.findTop3ByCreatedAtAfterOrderByLikeCountDesc(yesterday);

        List<BoardSimpleResponseDto> responseDtoList = bestBoards.stream()
                .map(BoardSimpleResponseDto::new)
                .collect(Collectors.toList());

        redisTemplateForCaching.opsForValue().set(BEST_BOARD_KEY, responseDtoList, Duration.ofMinutes(5));

        return new BoardBestResponseDto(responseDtoList);
    }
}

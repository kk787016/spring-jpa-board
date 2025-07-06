package com.example.boardHub.board.service;

import com.example.boardHub.board.dto.request.CommentRequestDto;
import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import com.example.boardHub.board.repository.BoardRepository;
import com.example.boardHub.board.repository.CommentRepository;
import com.example.boardHub.global.exception.CommentNotFoundException;
import com.example.boardHub.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @Transactional
    public void createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {

        Board board = boardService.findBoardForComment(boardId);

        Comment newComment = Comment.builder()
                .content(commentRequestDto.getContent())
                .board(board)
                .user(user)
                .parent(null)
                .build();

        board.addComment(newComment);
        commentRepository.save(newComment);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {

        Comment comment = findCommentAndCheckOwnership(commentId, user);

        if (commentRequestDto.getContent() != null) {
            comment.updateContent(commentRequestDto.getContent());
        }

    }

    @Transactional
    public void deleteComment(Long commentId, User user) {

        Comment comment = findCommentAndCheckOwnership(commentId, user);
/*        Comment parent = comment.getParent();*/

//        리팩토링 가능
        if (!comment.isLastChild()){
            comment.softDelete();
            return;
        }
        commentRepository.delete(comment);


        //        if (parent == null) {
//            if (comment.getChildren().isEmpty()) {
//                commentRepository.delete(comment);
//            } else {
//                comment.softDelete();
//            }
//        } else { // 내가 마지막 자식이라면?
//            boolean isLastChild = parent.isDeleted() && parent.getChildren().size() == 1;
//
//            commentRepository.delete(comment);
//
//            if (isLastChild) {
//                commentRepository.delete(parent);
//            }
//        }
    }

    @Transactional
    public void registerReplyComment(Long boardId, Long parentCommentId, CommentRequestDto commentRequestDto, User user) {
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() ->
                new CommentNotFoundException("부모 댓글을 찾을 수 없습니다.")
        );

        Board board = boardService.findBoardForComment(boardId);

        Comment replyComment = Comment.builder()
                .content(commentRequestDto.getContent())
                .board(board)
                .user(user)
                .parent(parentComment)
                .build();

        parentComment.addChildComment(replyComment);

        board.addComment(replyComment);

        commentRepository.save(replyComment);
    }


    private Comment findCommentAndCheckOwnership(Long commentId, User user) {

        Comment comment = commentRepository.findCommentsWithBoardAndUser(commentId).orElseThrow(() ->
                new CommentNotFoundException("해당 댓글을 찾을 수 없습니다.")
        );

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 댓글에 대한 권한이 없습니다.");
        }

        return comment;
    }


}

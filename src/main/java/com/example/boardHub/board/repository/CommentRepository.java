package com.example.boardHub.board.repository;

import com.example.boardHub.board.model.Board;
import com.example.boardHub.board.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    @Query("SELECT c FROM Comment c JOIN FETCH c.board b JOIN FETCH c.user WHERE c.id = :id AND c.deleted = false")
    Optional<Comment> findCommentsWithBoardAndUser(Long id);

}

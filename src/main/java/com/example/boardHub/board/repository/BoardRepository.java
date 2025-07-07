package com.example.boardHub.board.repository;

import com.example.boardHub.board.model.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);


    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.deleted = false ORDER BY b.createdAt DESC")
    List<Board> findByDeletedFalseOrderByCreatedAtDescWithUser();

    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id AND b.deleted = false")
    Optional<Board> findByIdAndDeletedFalseWithUser(Long id);

//    @Query("SELECT b FROM Board b WHERE b.createdAt >= :yesterday ORDER BY b.likeCount DESC")
//    List<Board> findTop3BestPostsLast24Hours(@Param("yesterday") LocalDateTime yesterday, Pageable pageable);

    List<Board> findTop3ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime createdAt);


}
package com.example.boardHub.board.repository;

import com.example.boardHub.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);


    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.deleted = false ORDER BY b.createdAt DESC")
    List<Board> findByDeletedFalseOrderByCreatedAtDescWithUser();

    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id AND b.deleted = false")
    Optional<Board> findByIdAndDeletedFalseWithUser(Long id); // 메서드명 변경 및 파라미터 :id 지정

}

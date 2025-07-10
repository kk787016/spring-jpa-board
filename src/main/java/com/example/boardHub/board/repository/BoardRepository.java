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


    //모든 게시글 표출, 유저 펫치조인 하는 이유는 화면 표출에 로그인 유저 쓰이기 대문에.
    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.deleted = false ORDER BY b.createdAt DESC")
    List<Board> findByDeletedFalseOrderByCreatedAtDescWithUser();

    //게시글 업데이트
    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id AND b.deleted = false")
    Optional<Board> findByIdAndDeletedFalseWithUser(Long id);

    //게시글 디테일 조회용
    @Query("SELECT distinct b FROM Board b LEFT JOIN FETCH b.comments WHERE b.id = :id AND b.deleted = false")
    Optional<Board> findByIdAndDeletedFalseWithComments(Long id);

//    @Query("SELECT b FROM Board b WHERE b.createdAt >= :yesterday ORDER BY b.likeCount DESC")
//    List<Board> findTop3BestPostsLast24Hours(@Param("yesterday") LocalDateTime yesterday, Pageable pageable);

    // 시간별 추천 랭킹
    List<Board> findTop3ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime createdAt);


}
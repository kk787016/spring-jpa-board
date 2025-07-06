package com.example.boardHub.board.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor

public class LikeCountScheduler {

    private final StringRedisTemplate redisTemplate;
    private final BoardRepository boardRepository;


    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void syncBoardCount(){

        System.out.println("### 추천/비추천 수 DB 동기화 스케줄러 시작 ###");

        Set<String> likeKeys = redisTemplate.keys("board:*:like");


        for (String likeKey : likeKeys) {
            try {
                // 키에서 boardId 추출 (예: "board:123:likes" -> "123")
                long boardId = Long.parseLong(likeKey.split(":")[1]);
                String dislikeKey = "board:" + boardId + ":dislike";

                // Redis에서 최신 카운트 조회
                Long likeCount = redisTemplate.opsForSet().size(likeKey);
                Long dislikeCount = redisTemplate.opsForSet().size(dislikeKey);

                // DB의 Board 엔티티에 반영
                boardRepository.findById(boardId).ifPresent(board -> {
                    board.updateLikeCount(likeCount.intValue(), dislikeCount.intValue());
                });

            } catch (Exception e) {
                // 특정 키 처리 중 오류 발생 시 로그 남기고 계속 진행
                System.err.println("DB 동기화 오류 발생: " + likeKey + ", " + e.getMessage());
            }
        }
        System.out.println("### 추천/비추천 수 DB 동기화 스케줄러 종료 ###");

    }

}

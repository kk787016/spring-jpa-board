package com.example.boardHub.board.repository;

import com.example.boardHub.board.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class ViewCountScheduler {

    private final RedisTemplate<String, Long> redisTemplate;
    private final BoardRepository boardRepository;

    @Scheduled(cron = "*/20 20 * * * *")
    @Transactional
    public void flushViewCountToDB() {

        System.out.println("### 게시글 DB 동기화 스케줄러 시작 ###");

        Set<String> keys = redisTemplate.keys("board:view*");

        if (keys.isEmpty()) return;

        Map<Long, Long> viewCountMap = new HashMap<>();
        for (String key : keys) {
            try {
                Long incrementCount = redisTemplate.opsForValue().getAndDelete(key);

                if (incrementCount != null && incrementCount > 0) {

                    long boardId = Long.parseLong(key.replace("board:view", ""));
                    viewCountMap.put(boardId, incrementCount);

                }
            } catch (Exception e) {
                log.error("조회수 키 처리 중 오류 발생: {}", key, e);
            }
        }

        if (viewCountMap.isEmpty()) return;

        List<Long> boardIds = new java.util.ArrayList<>(viewCountMap.keySet());
        List<Board> boardsToUpdate = boardRepository.findAllById(boardIds);

        for (Board board : boardsToUpdate) {
            long incrementCount = viewCountMap.getOrDefault(board.getId(), 0L);
            board.updateViewCount(board.getViewCount() + incrementCount);
        }

        boardRepository.saveAll(boardsToUpdate);

        log.info("### {}개의 게시글 조회수 동기화 완료 ###", boardsToUpdate.size());
        System.out.println("### 게시글 DB 동기화 스케줄러 종료 ###");

    }
}

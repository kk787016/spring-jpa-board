package com.example.boardHub.board.repository;

import com.example.boardHub.board.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class ViewCountScheduler {

    private final RedisTemplate<String, Long> redisTemplate;
    private final BoardRepository boardRepository;

    @Scheduled(cron = "*/20 * * * * *")
    @Transactional
    public void flushViewCountToDB() {
        Set<String> keys = redisTemplate.keys("board:view*");

        if (keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            Long incrementCount = redisTemplate.opsForValue().get(key);
            if (incrementCount == null || incrementCount == 0) continue;

            Long boardId = Long.valueOf(key.replace("board:view", ""));

            Board board = boardRepository.findById(boardId).orElse(null);
            if (board == null) {
                redisTemplate.delete(key);
                continue;
            }

            board.updateViewCount(board.getViewCount() + incrementCount);
            boardRepository.save(board);

            redisTemplate.delete(key);
        }
    }
}

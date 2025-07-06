package com.example.boardHub.board.service;

import com.example.boardHub.board.model.RecommendationType;
import com.example.boardHub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final StringRedisTemplate redisTemplate;


    public Map<String, Object> processRecommendation(Long boardId, Long userId, RecommendationType type) {

        String userStr = String.valueOf(userId);

        String primaryKey = "board:" + boardId + ":" + (type == RecommendationType.LIKE ? "like" : "dislike");
        String secondaryKey = "board:" + boardId + ":" + (type == RecommendationType.LIKE ? "dislike" : "like");

        boolean isAlreadyVoted = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(primaryKey, userStr));

        log.info("isAlreadyVoted: " + isAlreadyVoted);
        if (isAlreadyVoted) {
            redisTemplate.opsForSet().remove(primaryKey, userStr);
            return getResult(boardId, userId);
        }
        redisTemplate.opsForSet().add(primaryKey,userStr);
        redisTemplate.opsForSet().remove(secondaryKey,userStr);
        return getResult(boardId, userId);

    }

    private Map<String, Object> getResult(Long boardId, Long userId) {
        String userStr = String.valueOf(userId);
        String likeKey = "board:" + boardId + ":like";
        String dislikeKey = "board:" + boardId + ":dislike";

        Long likeCount = redisTemplate.opsForSet().size(likeKey);
        Long dislikeCount = redisTemplate.opsForSet().size(dislikeKey);

        log.info("like count: {}", likeCount);
        log.info("dislike count: {}", dislikeCount);

        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount != null ? likeCount : 0);
        response.put("dislikeCount", dislikeCount != null ? dislikeCount : 0);
        response.put("userLiked", Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeKey, userStr)));
        response.put("userDisLiked", Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(dislikeKey, userStr)));
        return response;
    }


}

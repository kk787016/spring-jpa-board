package com.example.boardHub.user.repository;

import com.example.boardHub.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUsername(String username);
}
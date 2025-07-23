package com.example.boardHub.user.repository;
import com.example.boardHub.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserId(String userId);

//    @Query("SELECT u.id FROM User u WHERE u.userId = :userId")
//    Optional<Long> findIdByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);
}

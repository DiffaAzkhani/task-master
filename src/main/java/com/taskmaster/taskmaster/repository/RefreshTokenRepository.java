package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.RefreshToken;
import com.taskmaster.taskmaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserAndExpiredAtBefore(User user, LocalDateTime now);

}

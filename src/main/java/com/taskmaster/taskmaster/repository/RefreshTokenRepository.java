package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    boolean existsByToken(String refreshToken);

    Optional<RefreshToken> findByTokenAndIsBlacklistFalse(String token);

    List<RefreshToken> findByExpiredAtBeforeAndIsBlacklistTrue(LocalDateTime now);

    List<RefreshToken> findByUser_Id(Long userId);

    List<RefreshToken> findByUser_Username(String currentUser);


}

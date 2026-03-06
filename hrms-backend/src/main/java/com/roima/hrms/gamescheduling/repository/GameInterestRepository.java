package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.GameInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameInterestRepository extends JpaRepository<GameInterest,Long> {

    List<GameInterest> findUserByGameIdAndActiveTrue(Long gameId);
    List<GameInterest> findGameByUserIdAndActiveTrue(Long userId);
    Optional<GameInterest> findByUserIdAndGameId(Long userId,Long gameId);
    boolean existsByUserIdAndGameIdAndActiveTrue(Long userId, Long gameId);
}

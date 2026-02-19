package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.GameConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameConfigRepository extends JpaRepository<GameConfig, Long> {
    Optional<GameConfig> findByGameId(Long gameId);
}

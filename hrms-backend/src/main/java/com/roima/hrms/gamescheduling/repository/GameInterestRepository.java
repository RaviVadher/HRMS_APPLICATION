package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.GameInterest;
import com.roima.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameInterestRepository extends JpaRepository<GameInterest,Long> {

    List<GameInterest> findUserByGameId(Long gameId);
    List<GameInterest> findGameByUserId(Long userId);
}

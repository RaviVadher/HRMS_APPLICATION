package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameSlotRepository extends JpaRepository<GameSlot, Integer> {

    boolean existsByGameIdAndSlotDate(Long gameId, LocalDate slotDate);
    List<GameSlot> findByGameIdAndSlotDate(Long gameId, LocalDate slotDate);
}

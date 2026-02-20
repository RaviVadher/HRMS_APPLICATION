package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface GameSlotRepository extends JpaRepository<GameSlot, Long> {

    boolean existsByGameIdAndSlotDate(Long gameId, LocalDate slotDate);
    List<GameSlot> findByGameIdAndSlotDate(Long gameId, LocalDate slotDate);

    @Query("select s from GameSlot s where s.startTime = :time And s.slotDate= current_date")
    List<GameSlot> findSlotStartingIsNow(LocalTime time);


    @Query("select s from GameSlot s where s.endTime = :time And s.slotDate = current_date ")
    List<GameSlot> findFinishedSlots(LocalTime time);

}

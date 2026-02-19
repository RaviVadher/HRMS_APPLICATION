package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.gamescheduling.dto.SlotResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SlotService {

    void generateSlotForGames();

    void generateSlotForGame(Long gameId, LocalDate date);
    List<SlotResponseDto> getSlotForGame(Long gameId);
}

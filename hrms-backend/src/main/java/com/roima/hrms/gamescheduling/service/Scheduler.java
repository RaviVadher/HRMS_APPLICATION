package com.roima.hrms.gamescheduling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SlotService slotService;

    @Scheduled(cron = "0 18 16 * * *")
    public void generateSlot() {
        slotService.generateSlotForGames();
    }
}

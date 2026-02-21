package com.roima.hrms.gamescheduling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SlotService slotService;
    private final BookingService bookingService;

    @Scheduled(cron = "0 0 0 * * * ")
    public void generateSlot() {
        slotService.generateSlotForGames();
    }


//    @Scheduled(cron = "0 * * * * *")
//    public void changeStatus() {
//        bookingService.processWaitingBookings();
//    }
//
//    @Scheduled(cron = "0 * * * * *")
//    public void incComplated() {
//        bookingService.updateStatsAfterSlotEnd();
//    }
}

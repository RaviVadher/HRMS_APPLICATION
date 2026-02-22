package com.roima.hrms.gamescheduling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SlotService slotService;
    private final BookingService bookingService;

    @Scheduled(cron = "0 0 0 * * * ")
    public void generateSlot() {

        slotService.generateSlotForGames();
        log.info("new slot are generated");
    }


//    @Scheduled(cron = "0 * * * * *")
//    public void changeStatus() {
//        bookingService.processWaitingBookings();
//        log.info("waiting list employee status changed");

//    }
//
//    @Scheduled(cron = "0 * * * * *")
//    public void incComplated() {
//        bookingService.updateStatsAfterSlotEnd();
//        log.info("winner's stats updated");

//    }
}

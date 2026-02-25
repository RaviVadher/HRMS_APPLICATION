package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.gamescheduling.dto.UpcomingSlotDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SlotService slotService;
    private final BookingService bookingService;

    @Scheduled(cron = "0  * * * * * ")
    public void generateSlot() {

        slotService.generateSlotForGames();
        log.info("new slot are generated");
    }


    @Scheduled(cron = "0 * * * * *")
    public void changeStatus() {
        bookingService.processWaitingBookings();
        log.info("waiting list employee status changed");

    }

    @Scheduled(cron = "0 * * * * *")
    public void incComplated() {
        bookingService.updateStatsAfterSlotEnd();
        log.info("winner's stats updated");

    }

    @Getter
    private List<UpcomingSlotDto> list = new ArrayList<>();
    @Scheduled(fixedRate = 60000)
    public void refresh()
    {
        list = bookingService.upcoming();
    }
}

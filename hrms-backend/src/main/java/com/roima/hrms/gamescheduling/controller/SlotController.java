package com.roima.hrms.gamescheduling.controller;

import com.roima.hrms.gamescheduling.dto.*;
import com.roima.hrms.gamescheduling.service.BookingService;
import com.roima.hrms.gamescheduling.service.Scheduler;
import com.roima.hrms.gamescheduling.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slot")
public class SlotController {

    private final SlotService slotService;
    private final BookingService bookingService;
    private final Scheduler scheduler;

    public SlotController(SlotService slotService, BookingService bookingService,Scheduler scheduler) {
        this.slotService = slotService;
        this.bookingService = bookingService;
        this.scheduler = scheduler;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('Hr')")
    public ResponseEntity<String> generateSlotForGames(){

        slotService.generateSlotForGames();
        return ResponseEntity.ok().body("Slots generated successfully");
    }

    @GetMapping("/{gameId}")
    public List<SlotResponseDto> getSlotsByGameId(@PathVariable("gameId") Long gameId){

        return  slotService.getSlotForGame(gameId);
    }


    @PostMapping("/booking")
    public BookingResponseDto bookSlot(@RequestBody BookingRequestDto dto){

        return bookingService.bookSlot(dto);
    }

    @PatchMapping("/conformation")
    public ResponseEntity<String> updateSlotStatus(){

        bookingService.processWaitingBookings();
        return ResponseEntity.ok().body("Slots updated successfully");
    }

    @PatchMapping("/incComplete")
    public ResponseEntity<String> updateSlotComplete(){

        bookingService.updateStatsAfterSlotEnd();
        return ResponseEntity.ok().body("players stats updated successfully");
    }

    @PatchMapping("/{bookedId}/cancel")
    public ResponseEntity<String> cancelSlot(@PathVariable("bookedId") Long bookedId){

        bookingService.cancelSlot(bookedId);
        return ResponseEntity.ok().body("slot cancelled successfully");
    }

    @GetMapping("game/{gameId}/{userId}/getHistory")
    public List<BookingHistoryResponseDto> getHistory(@PathVariable Long gameId,@PathVariable Long userId ){

        return bookingService.findHistory(gameId, userId);

    }

    @GetMapping("/dashboard/upcoming")
    public List<UpcomingSlotDto> data()
    {
        return scheduler.getList();
    }

}

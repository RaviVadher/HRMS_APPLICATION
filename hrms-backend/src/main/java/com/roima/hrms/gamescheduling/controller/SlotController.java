package com.roima.hrms.gamescheduling.controller;

import com.roima.hrms.gamescheduling.dto.SlotResponseDto;
import com.roima.hrms.gamescheduling.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slot")
public class SlotController {

    private final SlotService slotService;
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
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

}

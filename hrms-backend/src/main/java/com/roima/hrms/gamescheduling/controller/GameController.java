package com.roima.hrms.gamescheduling.controller;

import com.roima.hrms.gamescheduling.dto.GameConfigRequestDto;
import com.roima.hrms.gamescheduling.dto.GameConfigResponseDto;
import com.roima.hrms.gamescheduling.dto.GameInterestUserDto;
import com.roima.hrms.gamescheduling.dto.GameResponseDto;
import com.roima.hrms.gamescheduling.service.GameService;
import com.roima.hrms.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('Hr')")
    public ResponseEntity<String> createGame(@RequestParam String gameName)
    {
        return gameService.createGame(gameName);
    }


    @GetMapping("/getAll")
    public List<GameResponseDto> getAllGames()
    {
        return gameService.getAllGames();
    }


    @PostMapping("/{gameId}/gameConfig")
    @PreAuthorize("hasRole('Hr')")
    public ResponseEntity<String> configGame(@PathVariable Long gameId, @RequestBody GameConfigRequestDto dto)
    {
        return gameService.configGame(gameId,dto);
    }

    @GetMapping("/{gameId}/gameConfig")
    public GameConfigResponseDto getConfigGame(@PathVariable Long gameId)
    {
        return gameService.getConfigGame(gameId);
    }


    @PatchMapping("/{gameId}/gameConfig")
    @PreAuthorize("hasRole('Hr')")
    public ResponseEntity<String> updateConfigGame(@PathVariable Long gameId, @RequestBody GameConfigRequestDto dto)
    {
        return gameService.updateConfigGame(gameId,dto);
    }


    @PostMapping("/{gameId}/gameInterest")
    public ResponseEntity<String> gameInterest(@PathVariable Long gameId)
    {
        return gameService.interestedGame(gameId);
    }


    @GetMapping("/{gameId}/gameInterest")
    public List<GameInterestUserDto> getGameInterested(@PathVariable Long gameId)
    {
        return gameService.getGameInterested(gameId);
    }


    @GetMapping("/gameInterest/my")
    public List<GameInterestUserDto> getMyGameInterested()
    {
        return gameService.getMyGameInterested();
    }


}

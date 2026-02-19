package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.gamescheduling.dto.GameConfigRequestDto;
import com.roima.hrms.gamescheduling.dto.GameConfigResponseDto;
import com.roima.hrms.gamescheduling.dto.GameInterestUserDto;
import com.roima.hrms.gamescheduling.dto.GameResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GameService {

   ResponseEntity<String> createGame(String gameName);
    List<GameResponseDto> getAllGames();
    ResponseEntity<String> configGame(Long gameId, GameConfigRequestDto dto);
    GameConfigResponseDto getConfigGame(Long gameId);
    ResponseEntity<String> updateConfigGame(Long gameId,GameConfigRequestDto dto);
    ResponseEntity<String> interestedGame(Long gameId);
    List<GameInterestUserDto> getGameInterested(Long gameId);
    List<GameInterestUserDto> getMyGameInterested();

}

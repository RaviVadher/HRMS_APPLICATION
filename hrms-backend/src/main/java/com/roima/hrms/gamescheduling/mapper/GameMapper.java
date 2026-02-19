package com.roima.hrms.gamescheduling.mapper;

import com.roima.hrms.gamescheduling.dto.GameResponseDto;
import com.roima.hrms.gamescheduling.entity.Game;

public class GameMapper {

    public static GameResponseDto toDto(Game game) {

        GameResponseDto gameResponseDto = new GameResponseDto();
        gameResponseDto.setGameId(game.getId());
        gameResponseDto.setGameName(game.getGameName());
        gameResponseDto.setActive(game.isActive());

        return gameResponseDto;
    }
}

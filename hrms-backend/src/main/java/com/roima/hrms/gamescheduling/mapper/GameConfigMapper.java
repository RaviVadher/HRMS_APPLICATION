package com.roima.hrms.gamescheduling.mapper;

import com.roima.hrms.gamescheduling.dto.GameConfigResponseDto;
import com.roima.hrms.gamescheduling.entity.GameConfig;

public class GameConfigMapper {

    public static GameConfigResponseDto toDto(GameConfig gameConfig) {
        GameConfigResponseDto dto = new GameConfigResponseDto();
        dto.setGameId(gameConfig.getId());
        dto.setCapacity(gameConfig.getMaxPlayers());
        dto.setStartTime(gameConfig.getStartTime());
        dto.setEndTime(gameConfig.getEndTime());
        dto.setSlotDuration(gameConfig.getSlotDuration());

        return dto;
    }


}

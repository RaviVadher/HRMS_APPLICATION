package com.roima.hrms.gamescheduling.mapper;

import com.roima.hrms.gamescheduling.dto.GameInterestUserDto;
import com.roima.hrms.gamescheduling.entity.GameInterest;

public class InterestUserMapper {

    public static GameInterestUserDto toDto(GameInterest interest){
        GameInterestUserDto dto = new GameInterestUserDto();

        dto.setGameId(interest.getGame().getId());
        dto.setUserId(interest.getUser().getId());
        dto.setInterestId(interest.getId());
        dto.setName(interest.getUser().getName());
        dto.setGameName(interest.getGame().getGameName());

        return dto;
    }
}

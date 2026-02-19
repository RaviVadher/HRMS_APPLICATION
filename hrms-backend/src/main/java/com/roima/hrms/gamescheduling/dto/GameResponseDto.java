package com.roima.hrms.gamescheduling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDto {

    private Long gameId;
    private String gameName;
    private boolean isActive;
}

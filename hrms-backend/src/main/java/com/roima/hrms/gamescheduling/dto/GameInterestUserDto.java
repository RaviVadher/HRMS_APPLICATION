package com.roima.hrms.gamescheduling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameInterestUserDto {

    private Long gameId;
    private String GameName;
    private Long userId;
    private String name;
    private Long interestId;
}

package com.roima.hrms.gamescheduling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpcomingSlotDto {
    private String game;
    private LocalDate date;
    private LocalTime start;
    private  LocalTime end;
    private String bookedBy;
    private List<String> players;
}

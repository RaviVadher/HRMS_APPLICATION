package com.roima.hrms.travel.dto;

import com.roima.hrms.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TravelResponseDto {

    private Long travelId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long createdBy;
    private String origin;
    private String destination;


}

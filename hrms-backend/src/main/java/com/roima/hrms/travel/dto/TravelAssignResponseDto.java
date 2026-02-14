package com.roima.hrms.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TravelAssignResponseDto {

    private Long travelId;
    private Long assignedUserId;
    private String assignedUserName;
    private LocalDate assignedDate;
    private String title;
    private String assignedBy;
    private String  destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long assignedId;

}

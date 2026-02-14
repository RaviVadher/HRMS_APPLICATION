package com.roima.hrms.travel.dto;

import com.roima.hrms.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TravelCreateRequestDto {

    private String title;
    private String description;
    private String origin;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;

}

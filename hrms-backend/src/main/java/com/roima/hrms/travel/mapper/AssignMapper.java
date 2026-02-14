package com.roima.hrms.travel.mapper;

import com.roima.hrms.travel.dto.TravelAssignResponseDto;
import com.roima.hrms.travel.dto.TravelResponseDto;
import com.roima.hrms.travel.entity.Travel;
import com.roima.hrms.travel.entity.TravelAssign;
import org.springframework.stereotype.Component;

@Component
public class AssignMapper {

    public static TravelAssignResponseDto toDto(TravelAssign travelAssign)
    {
        TravelAssignResponseDto dto = new TravelAssignResponseDto();
        dto.setAssignedUserId(travelAssign.getUser().getId());
        dto.setTravelId(travelAssign.getTravel().getId());
        dto.setAssignedDate(travelAssign.getTravel().getCreated_date());
        dto.setAssignedUserName(travelAssign.getUser().getName());
        dto.setAssignedId(travelAssign.getId());
        return dto;
    }

    public static TravelAssignResponseDto myTravel(TravelAssign travelAssign)
    {
        TravelAssignResponseDto dto = new TravelAssignResponseDto();
        dto.setAssignedUserId(travelAssign.getUser().getId());
        dto.setTravelId(travelAssign.getTravel().getId());
        dto.setAssignedBy(travelAssign.getTravel().getUser().getName());
        dto.setAssignedDate(travelAssign.getAssignedDate());
        dto.setDestination(travelAssign.getTravel().getDestination());
        dto.setTitle(travelAssign.getTravel().getTravel_title());
        dto.setEndDate(travelAssign.getTravel().getEnd_date());
        dto.setStartDate(travelAssign.getTravel().getStart_date());
        dto.setAssignedId(travelAssign.getId());
        return dto;
    }
}

package com.roima.hrms.travel.mapper;

import com.roima.hrms.travel.dto.TravelCreateRequestDto;
import com.roima.hrms.travel.dto.TravelResponseDto;
import com.roima.hrms.travel.entity.Travel;
import org.springframework.stereotype.Component;

@Component
public class TravelMapper {

    public Travel toEntity(TravelCreateRequestDto dto)
    {
        Travel travel = new Travel();
        travel.setTravel_title(dto.getTitle());
        travel.setTravel_description(dto.getDescription());
        travel.setStart_date(dto.getStartDate());
        travel.setEnd_date(dto.getEndDate());
        travel.setDestination(dto.getDestination());
        travel.setOrigin(dto.getOrigin());
        return travel;
    }

    public TravelResponseDto toDto(Travel travel)
    {
        TravelResponseDto dto = new TravelResponseDto();
        dto.setTitle(travel.getTravel_title());
        dto.setDescription(travel.getTravel_description());
        dto.setStartDate(travel.getStart_date());
        dto.setEndDate(travel.getEnd_date());
        dto.setDestination(travel.getDestination());
        dto.setOrigin(travel.getOrigin());
        dto.setCreatedBy(travel.getUser().getId());
        dto.setTravelId(travel.getId());
        return dto;
    }
}

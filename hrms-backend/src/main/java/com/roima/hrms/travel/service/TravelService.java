package com.roima.hrms.travel.service;

import com.roima.hrms.travel.dto.TravelAssignResponseDto;
import com.roima.hrms.travel.dto.TravelCreateRequestDto;
import com.roima.hrms.travel.dto.TravelResponseDto;
import com.roima.hrms.travel.entity.TravelAssign;

import java.util.List;

public interface TravelService {

    TravelResponseDto createTravel(TravelCreateRequestDto dto);
    List<TravelResponseDto> findAllTravels();
    TravelResponseDto findTravelById(Long travelId);
    TravelAssign assignTravel(Long travelId,Long userId);
    List<TravelAssignResponseDto> findAllTravelsAssign(Long travelId);
    List<TravelAssignResponseDto> findMyTravelsAssign();
    TravelAssignResponseDto findMyTravelsAssign(Long assignId);


}

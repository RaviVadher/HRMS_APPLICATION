package com.roima.hrms.travel.controller;

import com.roima.hrms.travel.dto.TravelAssignResponseDto;
import com.roima.hrms.travel.dto.TravelAssingnRequestDto;
import com.roima.hrms.travel.dto.TravelCreateRequestDto;
import com.roima.hrms.travel.dto.TravelResponseDto;
import com.roima.hrms.travel.entity.TravelAssign;
import com.roima.hrms.travel.service.TravelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5174/")
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    public TravelController( TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('Hr')")
    public List<TravelResponseDto> findAllTravels(){
        return travelService.findAllTravels();
    }


    @GetMapping("/{travel_id}")
    @PreAuthorize("hasRole('Hr')")
    public TravelResponseDto findAllTravels(@PathVariable Long travel_id){
        return travelService.findTravelById(travel_id);
    }

    //Hr Create traveling plan
    @PostMapping("/create")
    public TravelResponseDto createTravel(@RequestBody TravelCreateRequestDto dto){
        return travelService.createTravel(dto);
    }


    //Hr assign travel plan to specific employee
    @PostMapping("/{travelId}/assign/{user_id}")
    @PreAuthorize("hasRole('Hr')")
    public TravelAssign assignTravel(@PathVariable Long travelId, @PathVariable Long user_id)
    {
        return travelService.assignTravel(travelId,user_id);
    }


    //Hr can see all travel assigns
    @GetMapping("/{travel_id}/assigns/getAll")
    public List<TravelAssignResponseDto> findAllTravelsAssign(@PathVariable Long travel_id){
         return travelService.findAllTravelsAssign(travel_id);
    }


    @GetMapping("/my")
    public List<TravelAssignResponseDto> findMyTravelsAssign(){
        return travelService.findMyTravelsAssign();
    }

    @GetMapping("/my/{assignId}")
    public TravelAssignResponseDto findMyTravelsAssignById(@PathVariable Long assignId  ){
        return travelService.findMyTravelsAssign(assignId);
    }


}

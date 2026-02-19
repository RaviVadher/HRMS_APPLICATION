package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.dto.TravelAssignResponseDto;
import com.roima.hrms.travel.entity.SubmittedTravelDocs;
import com.roima.hrms.travel.entity.TravelAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TravelAssignRepository extends JpaRepository<TravelAssign, Long> {

    Optional<TravelAssign> findByUser_idAndTravel_id(Long userId, Long travelId);
    List<TravelAssign> findByTravel_id(Long travelId);
    List<TravelAssign> findByUser_id(Long userId);

    List<TravelAssign> findByUser_Manager_id(Long managerId);

}

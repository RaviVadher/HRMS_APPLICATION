package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, Long> {
}

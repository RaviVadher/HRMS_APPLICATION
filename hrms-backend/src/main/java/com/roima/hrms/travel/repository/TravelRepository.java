package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    @Query("SELECT t FROM Travel t ORDER BY t.created_date DESC")
    List<Travel> findAllOrderByCreatedDateAsc();
}

package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.SubmittedTravelDocs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmittedTravelDocumentRepository extends JpaRepository<SubmittedTravelDocs,Long> {
    List<SubmittedTravelDocs> findByTravelAssign_id(Long assignId);

}

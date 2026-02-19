package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.SubmittedTravelDocs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SubmittedTravelDocumentRepository extends JpaRepository<SubmittedTravelDocs,Long> {
    List<SubmittedTravelDocs> findByTravelAssign_id(Long assignId);

}

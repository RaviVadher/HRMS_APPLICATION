package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.RequiredDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, Long> {
    List<RequiredDocument> findByTravel_Id(Long travelId);
}

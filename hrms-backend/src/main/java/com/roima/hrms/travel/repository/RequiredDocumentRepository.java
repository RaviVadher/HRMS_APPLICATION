package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.RequiredDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, Long> {
    List<RequiredDocument> findByTravel_Id(Long travelId);
}

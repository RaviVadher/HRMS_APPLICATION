package com.roima.hrms.openjob.repository;

import com.roima.hrms.openjob.entity.JobCvReviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobCvReviewerRepository extends JpaRepository<JobCvReviewer, Long> {

    List<JobCvReviewer> findByJob_JobId(Long jobId);
}

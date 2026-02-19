package com.roima.hrms.openjob.repository;

import com.roima.hrms.openjob.entity.JobCvReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JobCvReviewerRepository extends JpaRepository<JobCvReviewer, Long> {

    List<JobCvReviewer> findByJob_JobId(Long jobId);
}

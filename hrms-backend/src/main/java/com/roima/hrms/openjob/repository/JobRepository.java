package com.roima.hrms.openjob.repository;

import com.roima.hrms.openjob.entity.Job;
import com.roima.hrms.openjob.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(JobStatus status);
}

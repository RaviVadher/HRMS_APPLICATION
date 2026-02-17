package com.roima.hrms.openjob.repository;

import com.roima.hrms.openjob.entity.SharedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedJobRepository extends JpaRepository<SharedJob, Long> {

    List<SharedJob> findByJob_JobId(Long jobId);

}

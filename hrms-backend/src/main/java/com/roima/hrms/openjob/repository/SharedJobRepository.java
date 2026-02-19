package com.roima.hrms.openjob.repository;

import com.roima.hrms.openjob.entity.SharedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SharedJobRepository extends JpaRepository<SharedJob, Long> {

    List<SharedJob> findByJob_JobId(Long jobId);

}

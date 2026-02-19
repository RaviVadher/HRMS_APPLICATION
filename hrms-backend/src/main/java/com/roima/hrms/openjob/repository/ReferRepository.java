package com.roima.hrms.openjob.repository;

import com.roima.hrms.openjob.entity.Refer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferRepository extends JpaRepository<Refer, Long> {

    List<Refer> findByJob_JobId(Long jobId);
}

package com.roima.hrms.openjob.service;

import com.roima.hrms.openjob.dto.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface JobService {

     JobDto createJob(JobCreateDto dto);
     List<JobDto> getAllJobs();
     JobDto  getJobById(Long id);
     ResponseEntity<String> shareJob(Long jobId,String email);
     List<SharedJobResponseDto> findAllShered(Long jobId);
     List<SharedJobResponseDto> findMySharedJob(Long jobId);
     List<ReferJobResponseDto> findMyReferredJob(Long jobId);
     void refer(ReferRequestDto dto);
     List<ReferJobResponseDto> getRefer(Long jobId);
     Resource downloadCv(Long referId);
     ResponseEntity<String> changeJobStatus(Long jobId,ChangeStatusDto dto);
}

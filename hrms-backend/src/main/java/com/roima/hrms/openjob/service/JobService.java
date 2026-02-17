package com.roima.hrms.openjob.service;

import com.roima.hrms.openjob.dto.JobCreateDto;
import com.roima.hrms.openjob.dto.JobDto;
import com.roima.hrms.openjob.dto.ReferRequestDto;
import com.roima.hrms.openjob.dto.SharedJobResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface JobService {


     JobDto createJob(JobCreateDto dto, MultipartFile file);
     List<JobDto> getAllJobs();
     JobDto  getJobById(Long id);
     ResponseEntity<String> shareJob(Long jobId,String email);
     List<SharedJobResponseDto> findAllShered(Long jobId);
     void refer(ReferRequestDto dto, MultipartFile file);
}

package com.roima.hrms.openjob.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roima.hrms.openjob.dto.JobCreateDto;
import com.roima.hrms.openjob.dto.JobDto;
import com.roima.hrms.openjob.dto.ReferRequestDto;
import com.roima.hrms.openjob.dto.SharedJobResponseDto;
import com.roima.hrms.openjob.service.JobService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class OpenJobController {


    private JobService jobService;
    public OpenJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(value = "/create",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('Hr')")
    public JobDto createJob(@RequestPart("data") String json, @RequestPart("jd") MultipartFile file) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        JobCreateDto dto = mapper.readValue(json, JobCreateDto.class);
        return jobService.createJob(dto,file);
    }

    @GetMapping("/allJob")
    @PreAuthorize("hasRole('Hr')")
    public List<JobDto> getAllJob()
    {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Hr','Employee','Manager')")
    public JobDto getJobById(@PathVariable Long id)
    {
        return jobService.getJobById(id);
    }


    @PostMapping("/{id}/share")
    @PreAuthorize("hasAnyRole('Hr','Employee','Manager')")
    public ResponseEntity<String> shareJob(@PathVariable Long id, @RequestParam String email)
    {
         return jobService.shareJob(id,email);
    }

    @GetMapping("/{job_id}/allShared")
    @PreAuthorize("hasRole('Hr')")
    public List<SharedJobResponseDto> shareAllJob(@PathVariable Long job_id)
    {
        return jobService.findAllShered(job_id);
    }


    @PostMapping(value = "/refer",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> referJob(@RequestPart("data") String json, @RequestPart("cv") MultipartFile file) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        ReferRequestDto dto = mapper.readValue(json, ReferRequestDto.class);
        System.out.println(file.getOriginalFilename() + "------------------------------------**********************");
        jobService.refer(dto,file);
        return ResponseEntity.ok("refered submitted");

    }
}

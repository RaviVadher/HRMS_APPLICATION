package com.roima.hrms.openjob.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roima.hrms.openjob.dto.*;
import com.roima.hrms.openjob.enums.JobStatus;
import com.roima.hrms.openjob.exception.InvalideFileFormateException;
import com.roima.hrms.openjob.service.JobService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
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


    //Create Job
    @PostMapping(value = "/create",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('Hr')")
    public JobDto createJob(@ModelAttribute JobCreateDto dto)
    {
        if(!"application/pdf".equals(dto.getFile().getContentType()))
        {
            throw new InvalideFileFormateException("JD must be in Pdf format");
        }
        return jobService.createJob(dto);
    }

    //get all job
    @GetMapping("/allJob")
    @PreAuthorize("hasAnyRole('Hr','Employee','Manager')")
    public List<JobDto> getAllJob()
    {
        return jobService.getAllJobs();
    }

    //get job details
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Hr','Employee','Manager')")
    public JobDto getJobById(@PathVariable Long id)
    {
        return jobService.getJobById(id);
    }


    //share job
    @PostMapping("/{id}/share")
    @PreAuthorize("hasAnyRole('Hr','Employee','Manager')")
    public ResponseEntity<String> shareJob(@PathVariable Long id, @RequestParam("email") String email)
    {
        return jobService.shareJob(id,email);
    }

    @GetMapping("/{job_id}/allShared")
    public List<SharedJobResponseDto> shareAllJob(@PathVariable Long job_id)
    {
        return jobService.findAllShered(job_id);
    }


    @PostMapping(value = "/refer",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> referJob(@ModelAttribute ReferRequestDto dto)
    {
        if(!"application/pdf".equals(dto.getFile().getContentType()))
        {
            throw new InvalideFileFormateException("CV must be in Pdf format");
        }
        jobService.refer(dto);
        return ResponseEntity.ok("refer submitted");

    }

    @GetMapping("/{jobId}/refers")
    public List<ReferJobResponseDto> getRefereJob(@PathVariable Long jobId)
    {
        return jobService.getRefer(jobId);
    }

    @GetMapping("/refer/{referId}/downloadCv")
    public ResponseEntity<Resource> downloadProof(@PathVariable Long referId) {

        Resource file = jobService.downloadCv(referId);

        MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return  ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PatchMapping(value = "/{jobId}/chageStatus")
    @PreAuthorize("hasRole('Hr')")
    public ResponseEntity<String> changeJobStatus(@PathVariable Long jobId, @RequestBody ChangeStatusDto dto)
    {
        return jobService.changeJobStatus(jobId,dto);
    }

}

package com.roima.hrms.openjob.mapper;

import com.roima.hrms.openjob.dto.JobDto;
import com.roima.hrms.openjob.entity.Job;
import com.roima.hrms.openjob.enums.JobStatus;

import java.time.LocalDateTime;

public class JobMapper {

    public static JobDto toDto(Job job) {
        JobDto dto = new JobDto();

        dto.setJobId(job.getJobId());
        dto.setSummary(job.getJob_summary());
        dto.setTitle(job.getTitle());
        dto.setStatus(job.getStatus());
        dto.setHrMail(job.getEmail_hr());
        dto.setCreatedBy(job.getCreatedBy().getName());
        dto.setCreatedOn(job.getCreated_at());
        dto.setUpdatedOn(job.getUpdated_at());

        return dto;
    }
}

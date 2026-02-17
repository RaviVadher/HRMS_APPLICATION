package com.roima.hrms.openjob.dto;

import com.roima.hrms.openjob.enums.JobStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class JobDto {

    private Long jobId;
    private String title;
    private String summary;
    private JobStatus status;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String hrMail;

}

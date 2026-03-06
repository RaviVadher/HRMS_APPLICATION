package com.roima.hrms.openjob.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedJobResponseDto {

    private Long jobId;
    private String sharedBy;
    private LocalDateTime sharedDate;
    private String sharedEmail;
}

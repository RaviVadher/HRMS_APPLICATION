package com.roima.hrms.openjob.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReferJobResponseDto {
        private Long referId;
        private String friendName;
        private String friendEmail;
        private String note;
        private String sharedBy;
        private LocalDateTime sharedDate;
        private String cvPath;
    }


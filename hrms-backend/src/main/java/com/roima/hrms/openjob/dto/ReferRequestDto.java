package com.roima.hrms.openjob.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReferRequestDto {
    private Long jobId;
    private String friendName;
    private String friendEmail;
    private String note;
}

package com.roima.hrms.openjob.dto;

import com.roima.hrms.openjob.enums.JobStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusDto {

    private JobStatus status;
}

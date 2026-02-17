package com.roima.hrms.openjob.mapper;

import com.roima.hrms.openjob.dto.SharedJobResponseDto;
import com.roima.hrms.openjob.entity.SharedJob;

public class SharedJobMapper {


    public static SharedJobResponseDto toDto(SharedJob sharedJob) {

        SharedJobResponseDto dto = new SharedJobResponseDto();

        dto.setSharedEmail(sharedJob.getShared_email());
        dto.setSharedBy(sharedJob.getSharedby().getName());
        dto.setJobId(sharedJob.getPk_shared_job());
        dto.setSharedDate(sharedJob.getShared_at());
        return dto;
    }
}

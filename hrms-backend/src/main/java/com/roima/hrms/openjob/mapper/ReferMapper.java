package com.roima.hrms.openjob.mapper;

import com.roima.hrms.openjob.dto.ReferJobResponseDto;
import com.roima.hrms.openjob.entity.Refer;

import java.time.LocalDateTime;

public class ReferMapper {


    public static ReferJobResponseDto toDto(Refer refer)
    {
        ReferJobResponseDto dto = new ReferJobResponseDto();

        dto.setReferId(refer.getRefer_id());
        dto.setFriendEmail(refer.getRefer_email());
        dto.setFriendName(refer.getRefer_name());
        dto.setNote(refer.getRefer_description());
        dto.setCvPath(refer.getRefer_cvpath());
        dto.setSharedBy(refer.getUser().getName());
        dto.setSharedDate(refer.getRefer_date());
        return dto;
    }
}

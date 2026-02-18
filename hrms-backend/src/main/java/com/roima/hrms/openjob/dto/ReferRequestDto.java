package com.roima.hrms.openjob.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReferRequestDto {
    private Long jobId;
    private String friendName;
    private String friendEmail;
    private String note;
    private MultipartFile file;
}

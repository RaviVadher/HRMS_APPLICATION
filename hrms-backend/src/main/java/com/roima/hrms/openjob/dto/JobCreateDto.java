package com.roima.hrms.openjob.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class JobCreateDto {

    private String title;
    private String summary;
    private List<Long> reviewerIds;
    private MultipartFile file;

}

package com.roima.hrms.openjob.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class JobCreateDto {

    private String title;
    private String summary;
    private List<Long> reviewerIds;

}

package com.roima.hrms.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.core.io.Resource;


@Getter
@Setter
public class SubmitedDocumentDto {

    private Long documentId;
    private String documentName;
    private LocalDateTime submittedDate;
    private Resource  documentImage;
}

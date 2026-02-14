package com.roima.hrms.travel.mapper;

import com.roima.hrms.travel.dto.SubmitedDocumentDto;
import com.roima.hrms.travel.entity.SubmittedTravelDocs;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentMapper {

    public static  SubmitedDocumentDto toDto(SubmittedTravelDocs documents) {
        SubmitedDocumentDto dto = new SubmitedDocumentDto();
        dto.setDocumentId(documents.getId());
        dto.setSubmittedDate(documents.getUpload_at());
        dto.setDocumentName(documents.getDocumentName());
        //dto.setDocumentImage(documentImage);
        return  dto;
    }
}

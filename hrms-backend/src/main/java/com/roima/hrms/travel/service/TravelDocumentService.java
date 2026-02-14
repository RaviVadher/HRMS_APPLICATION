package com.roima.hrms.travel.service;
import com.roima.hrms.travel.dto.RequiredTravelDocumentRequestDto;
import com.roima.hrms.travel.dto.RequiredTravelDocumentResponseDto;
import com.roima.hrms.travel.dto.SubmitedDocumentDto;
import com.roima.hrms.travel.entity.RequiredDocument;
import com.roima.hrms.travel.entity.SubmittedTravelDocs;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface TravelDocumentService {

    void uploadDocument(Long assignedId,String docName, MultipartFile file,String filetype);
    Resource downloadDocument(Long documentId);
    RequiredDocument postDocument(Long travelId, RequiredTravelDocumentRequestDto dto);
    List<RequiredDocument>  getDocument(Long travelId);

     List<SubmitedDocumentDto> findByAssigned_id(Long assignedId);
}

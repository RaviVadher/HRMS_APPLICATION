package com.roima.hrms.travel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequiredTravelDocumentResponseDto {

    private Long requiredDocumentId;
    private String documentName;
    private Long travelId;
}

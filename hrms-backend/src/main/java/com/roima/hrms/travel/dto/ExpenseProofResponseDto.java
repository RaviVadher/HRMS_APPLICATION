package com.roima.hrms.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseProofResponseDto {

    private Long proofId;
    private String downloadsUrl;
    private LocalDateTime uploadDate;
}

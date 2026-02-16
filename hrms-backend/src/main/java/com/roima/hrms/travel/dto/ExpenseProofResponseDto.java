package com.roima.hrms.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseProofResponseDto {

    private Long proofId;
    private String downloadsUrl;
    private LocalDateTime uploadDate;
}

package com.roima.hrms.travel.dto;

import com.roima.hrms.travel.enums.ExpenseStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ExpenseResponseDto {

    private Long expenseId;
    private String category;
    private BigDecimal amount;
    private ExpenseStatus status;
    private LocalDate expenseDate;
    private LocalDate reviewedDate;
    private Long reviewerId;
    private String hrRemark;

    private List<ExpenseProofResponseDto> proofs;
}

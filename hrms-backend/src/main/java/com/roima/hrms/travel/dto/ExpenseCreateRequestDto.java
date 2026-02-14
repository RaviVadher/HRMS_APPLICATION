package com.roima.hrms.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseCreateRequestDto {

    private Long assignId;
    private String category;
    private LocalDate expenseDate;
    private BigDecimal amount;
}

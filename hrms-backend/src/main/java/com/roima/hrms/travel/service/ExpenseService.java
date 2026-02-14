package com.roima.hrms.travel.service;

import com.roima.hrms.travel.dto.ChangeExpenseStatusDto;
import com.roima.hrms.travel.dto.ExpenseCreateRequestDto;
import com.roima.hrms.travel.dto.ExpenseResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponseDto createExpense(Long travel_id, Long assignId, String category, BigDecimal amount, LocalDate date, MultipartFile file);
    List<ExpenseResponseDto> getExpenseDetail(Long assignId);
    ExpenseResponseDto changeStatus( Long expenseId, ChangeExpenseStatusDto dto);
}

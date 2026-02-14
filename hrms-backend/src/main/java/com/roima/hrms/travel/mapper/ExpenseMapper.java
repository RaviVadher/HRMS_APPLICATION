package com.roima.hrms.travel.mapper;

import com.roima.hrms.travel.dto.ExpenseProofResponseDto;
import com.roima.hrms.travel.dto.ExpenseResponseDto;
import com.roima.hrms.travel.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenseMapper {

    public static ExpenseResponseDto toDto(Expense expense) {
        ExpenseResponseDto dto = new ExpenseResponseDto();

        dto.setExpenseId(expense.getId());
        dto.setCategory(expense.getCategory());
        dto.setAmount(expense.getExpense_amount());
        dto.setExpenseDate(expense.getExpense_date());
        dto.setStatus(expense.getExpense_status());

        if(expense.getActionBy()!=null){
            dto.setReviewerId(expense.getActionBy().getId());
            dto.setReviewedDate(expense.getReviewed_date());
            dto.setHrRemark(expense.getHr_remarks());
        }

        if(expense.getExpensesProof()!=null){

            List<ExpenseProofResponseDto> downloadsUrl = expense.getExpensesProof()
                    .stream()
                    .map(proof -> {
                        ExpenseProofResponseDto prf = new ExpenseProofResponseDto();
                        prf.setProofId(proof.getId());
                        prf.setDownloadsUrl("/api/expense/proof/"+proof.getId());

                        return prf;
                    })
                    .toList();
            dto.setProofs(downloadsUrl);
        }

        return dto;
    }
}

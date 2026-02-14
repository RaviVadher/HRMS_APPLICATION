package com.roima.hrms.travel.dto;

import com.roima.hrms.travel.enums.ExpenseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeExpenseStatusDto {

    private ExpenseStatus status;
    private String hrRemark;

}

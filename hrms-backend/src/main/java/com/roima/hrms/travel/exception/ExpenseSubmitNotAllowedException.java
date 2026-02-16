package com.roima.hrms.travel.exception;

public class ExpenseSubmitNotAllowedException extends RuntimeException {

    public ExpenseSubmitNotAllowedException() {
        super("Expense submission date are exceeded ");
    }
}

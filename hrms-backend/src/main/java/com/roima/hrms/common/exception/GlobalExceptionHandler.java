package com.roima.hrms.common.exception;
import com.roima.hrms.mail.MailNotSendException;
import com.roima.hrms.openjob.exception.JobNotFoundException;
import com.roima.hrms.travel.exception.ExpenseSubmitNotAllowedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseSubmitNotAllowedException.class)
    public ResponseEntity<ApiError>expenseSubmitNotAllowed(ExpenseSubmitNotAllowedException ex) {
         return  buildError(ex.getMessage(),"Expense submission not allowed", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ApiError>jobNotFound(JobNotFoundException ex) {
        return  buildError(ex.getMessage(),"Job not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MailNotSendException.class)
    public ResponseEntity<ApiError>mailNotSend(MailNotSendException ex) {
        return  buildError(ex.getMessage(),"Mail not send", HttpStatus.NOT_ACCEPTABLE);
    }

    private ResponseEntity<ApiError> buildError(String msg, String error, HttpStatus status) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                error,
                msg
        );

        return ResponseEntity.status(status).body(apiError);
    }
}

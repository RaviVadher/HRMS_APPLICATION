package com.roima.hrms.common.exception;
import com.roima.hrms.gamescheduling.exception.ConfigExistException;
import com.roima.hrms.gamescheduling.exception.NotFoundException;
import com.roima.hrms.gamescheduling.exception.TimeOutException;
import com.roima.hrms.mail.MailNotSendException;
import com.roima.hrms.openjob.exception.JobNotFoundException;
import com.roima.hrms.travel.exception.AllReadyAssignedException;
import com.roima.hrms.travel.exception.ExpenseSubmitNotAllowedException;
import com.roima.hrms.travel.exception.WrongdateException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.GeneralSecurityException;
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

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseEntity<ApiError>userPrincipalNotFound(UserPrincipalNotFoundException ex) {
        return  buildError(ex.getMessage(),"User not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConfigExistException.class)
    public ResponseEntity<ApiError>configExist(ConfigExistException ex) {
        return  buildError(ex.getMessage(),"Config exist", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError>notFound(NotFoundException ex) {
        return  buildError(ex.getMessage(),"Not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongdateException.class)
    public ResponseEntity<ApiError>wrongDate(WrongdateException ex) {
        return  buildError(ex.getMessage(),"Wrong date", HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<ApiError>timeOut(TimeOutException ex) {
        return  buildError(ex.getMessage(),"Time out", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AllReadyAssignedException.class)
    public ResponseEntity<ApiError>allReadyAssigned(AllReadyAssignedException ex) {
        return  buildError(ex.getMessage(),"All ready assigned", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiError>general(AllReadyAssignedException ex) {
        return  buildError(ex.getMessage(),ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }


     @ExceptionHandler(Exception.class)
     public ResponseEntity<ApiError>handleAll(Exception ex) {
         Throwable root = ex;
         while (root.getCause() != null) {
             root = root.getCause();
         }
         return  buildError(ex.getMessage(),root.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

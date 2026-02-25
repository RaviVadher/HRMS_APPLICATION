package com.roima.hrms.travel.controller;

import com.roima.hrms.travel.dto.ChangeExpenseStatusDto;
import com.roima.hrms.travel.dto.ExpenseCreateRequestDto;
import com.roima.hrms.travel.dto.ExpenseProofResponseDto;
import com.roima.hrms.travel.dto.ExpenseResponseDto;
import com.roima.hrms.travel.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@RestController
@RequestMapping("/api/travels")
public class ExpenseController {

   private ExpenseService expenseService;
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping(value = "/{travel_id}/assign/expense",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ExpenseResponseDto createExpense(
            @PathVariable Long travel_id,
             @RequestParam("assignedId")Long assignId,
            @RequestParam("category") String category,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("expenseDate") LocalDate date,
            @RequestParam("file") MultipartFile file
    ) {
        return expenseService.createExpense(travel_id,assignId,category, amount,date,file);
    }


    @GetMapping("/{assign_id}/expense")
    public List<ExpenseResponseDto> getExpense(@PathVariable Long assign_id ){
        return expenseService.getExpenseDetail(assign_id);
    }

    @GetMapping("proof/{expense_id}")
    public List<ExpenseProofResponseDto> getExpenseProof(@PathVariable Long expense_id){

        return expenseService.getExpenseProofDetail(expense_id);
    }

    @PostMapping("assign/expense/{expense_id}/changestatus")
    @PreAuthorize("hasRole('Hr')")
    public ExpenseResponseDto changeStatus(@PathVariable Long expense_id,@RequestBody ChangeExpenseStatusDto dto){
        return expenseService.changeStatus(expense_id,dto);

    }

    @GetMapping("/{proofId}/downloadProofs")
    public ResponseEntity<Resource> downloadProof(@PathVariable Long proofId) {
        Resource file = expenseService.downloadProof(proofId);

        MediaType mediaType = MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return  ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFilename() + "\"").body(file);
    }



}

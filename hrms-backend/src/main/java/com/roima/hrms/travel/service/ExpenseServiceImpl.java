package com.roima.hrms.travel.service;

import com.roima.hrms.travel.dto.ChangeExpenseStatusDto;
import com.roima.hrms.travel.dto.ExpenseCreateRequestDto;
import com.roima.hrms.travel.dto.ExpenseResponseDto;
import com.roima.hrms.travel.entity.Expense;
import com.roima.hrms.travel.entity.ExpenseProof;
import com.roima.hrms.travel.entity.TravelAssign;
import com.roima.hrms.travel.enums.ExpenseStatus;
import com.roima.hrms.travel.mapper.ExpenseMapper;
import com.roima.hrms.travel.repository.ExpenseProofRepository;
import com.roima.hrms.travel.repository.ExpenseRepository;
import com.roima.hrms.travel.repository.TravelAssignRepository;
import com.roima.hrms.travel.repository.TravelRepository;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService{

    private final TravelAssignRepository travelAssignRepository;
    private final FileStorageService fileStorageService;
    private final ExpenseProofRepository expenseProofRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;

    public ExpenseServiceImpl(TravelAssignRepository travelAssignRepository,
                              FileStorageService fileStorageService,
                              ExpenseProofRepository expenseProofRepository,
                              ExpenseRepository expenseRepository,
                              ExpenseMapper expenseMapper,
                              UserRepository userRepository, TravelRepository travelRepository) {
        this.travelAssignRepository = travelAssignRepository;
        this.fileStorageService = fileStorageService;
        this.expenseProofRepository = expenseProofRepository;
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
    }

    @Override
    public ExpenseResponseDto createExpense(Long travel_id, Long assignId, String category, BigDecimal amount, LocalDate date, MultipartFile file)
    {
        Expense expense = new Expense();
        ExpenseProof expenseProof = new ExpenseProof();

        LocalDate entryDate = LocalDate.now();
        LocalDate start_date = travelRepository.findById(travel_id).get().getStart_date();
        LocalDate end_date = travelRepository.findById(travel_id).get().getEnd_date();

        if(!entryDate.isAfter(start_date) || entryDate.isAfter(end_date.plusDays(10))){

            throw new RuntimeException("Currently Expense submission is not allowed");
        }

        TravelAssign assign = travelAssignRepository.findById(assignId).orElseThrow(()->new RuntimeException("Invelid travel assign"));
        expense.setAssign(assign);
        expense.setExpense_date(date);
        expense.setExpense_amount(amount);
        expense.setCategory(category);
        expense.setExpense_status(ExpenseStatus.Submitted);

        expenseRepository.save(expense);
        String path = fileStorageService.store(
                file,
                travel_id,
                assign.getUser().getId(),
                "expense"
        );

        expenseProof.setFile_path(path);
        expenseProof.setExpense(expense);
        expenseProof.setUser(assign.getUser());

        expenseProofRepository.save(expenseProof);
        return expenseMapper.toDto(expense);
    }

    @Override
    public List<ExpenseResponseDto> getExpenseDetail(Long assignId) {
        List<Expense> expenses= expenseRepository.findAllByAssignIdWithProofs(assignId);

               return expenses.stream()
                .map(ExpenseMapper::toDto)
                .toList();
    }

    @Override
    public ExpenseResponseDto changeStatus(Long expenseId, ChangeExpenseStatusDto dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Invelide user"));
        Expense expense = expenseRepository.findById(expenseId).get();

        expense.setExpense_status(dto.getStatus());
        expense.setActionBy(user);
        expense.setHr_remarks(dto.getHrRemark());
        expense.setReviewed_date(LocalDate.now());
        expenseRepository.save(expense);

        return expenseMapper.toDto(expense);
    }
}

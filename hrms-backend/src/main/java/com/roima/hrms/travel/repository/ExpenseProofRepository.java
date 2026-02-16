package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.ExpenseProof;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseProofRepository extends JpaRepository<ExpenseProof,Long> {

    List<ExpenseProof> findByExpense_id(Long expense_id);
}

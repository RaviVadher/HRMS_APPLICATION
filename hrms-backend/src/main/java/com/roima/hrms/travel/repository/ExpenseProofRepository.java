package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.ExpenseProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseProofRepository extends JpaRepository<ExpenseProof,Long> {

    List<ExpenseProof> findByExpense_id(Long expense_id);
}

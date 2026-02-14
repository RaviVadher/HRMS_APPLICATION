package com.roima.hrms.travel.repository;

import com.roima.hrms.travel.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


     @Query("""
 SELECT DISTINCT e from Expense e LEFT  JOIN FETCH e.expensesProof where e.assign.Id= :assignId
 """)
     List<Expense> findAllByAssignIdWithProofs(@Param("assignId")Long assignId);
}
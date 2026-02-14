package com.roima.hrms.travel.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "expense_proof")
public class ExpenseProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proof_id")
    private Long Id;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name="fk_user_id")
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String file_path;


    private LocalDateTime uploaded_at = LocalDateTime.now();
}

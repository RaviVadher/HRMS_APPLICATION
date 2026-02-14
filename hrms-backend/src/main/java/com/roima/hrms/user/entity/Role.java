package com.roima.hrms.user.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_role_id")
    private Long Id;

    @NotBlank(message = "role name cannot be empty")
    @Column(nullable = false,unique = true)
    private String role;

}

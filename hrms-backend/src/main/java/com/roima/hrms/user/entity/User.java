package com.roima.hrms.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="pk_user_id")
    private Long Id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false, unique = true)
    private String password;

    @Email(message = "Email should be in right formate")
    @NotBlank(message = "mail cannot be empty")
    @Column(nullable = false, unique = true)
    private String email;

    @Past
    @NotBlank(message = "birthdate cannot be empty")
    @Column(nullable = false)
    private LocalDate birth_date;

    @PastOrPresent
    @NotBlank(message = "joining date cannot be empty")
    @Column(nullable = false)
    private LocalDate joining_date;

    @NotBlank
    @Column(nullable = false)
    private String designation;

    @Column
    private String profile_image;

    @ManyToOne
    @JoinColumn(name="fk_manager_id")
    private User manager;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_role_id")
    @JsonIgnore
    private Role role;

    @Column(nullable = false)
    private LocalDate creation_at;

    @Column
    private LocalDate updated_at;

    @PrePersist
    void onCreate() {
        creation_at = LocalDate.now();
    }

    @PreUpdate
    void onUpdate() {
        updated_at = LocalDate.now();
    }
}

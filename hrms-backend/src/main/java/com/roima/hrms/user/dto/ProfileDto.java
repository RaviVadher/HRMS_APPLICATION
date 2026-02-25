package com.roima.hrms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    private String userName;
    private String email;
    private LocalDate birthDate;
    private LocalDate joiningDate;
    private String designation;
}

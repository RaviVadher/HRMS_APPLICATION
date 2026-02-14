package com.roima.hrms.auth.dto;

import com.roima.hrms.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    private String name;
    private String password;
    private String email;
    private Role role;
    private String birthday;
    private String joinDate;

}

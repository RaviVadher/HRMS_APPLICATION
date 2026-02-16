package com.roima.hrms.user.dto;

import com.roima.hrms.user.entity.User;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgChartDto {

    private UserDto user;
    private List<UserDto> managerChain;
    private List<UserDto> directReports;
}

package com.roima.hrms.user.dto;

import com.roima.hrms.travel.dto.TravelAssignResponseDto;
import com.roima.hrms.travel.entity.TravelAssign;
import com.roima.hrms.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserResponceDto toDto(User user) {
        UserResponceDto dto = new UserResponceDto();
         dto.setUserId(user.getId());
         dto.setRolename(user.getRole().getRole());
         dto.setUsername(user.getName());
         return dto;
    }

}


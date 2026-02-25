package com.roima.hrms.user.controller;

import com.roima.hrms.user.dto.OrgChartDto;
import com.roima.hrms.user.dto.ProfileDto;
import com.roima.hrms.user.dto.UserDto;
import com.roima.hrms.user.service.OrgChartService;
import com.roima.hrms.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orgchart")
public class OrgChartController {

    private OrgChartService orgChartService;
    private UserService userService;

    public OrgChartController(OrgChartService orgChartService,UserService userService
    ) {
        this.orgChartService = orgChartService;
        this.userService= userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Hr','Manager','Employee')")
    public OrgChartDto getOrgChart(@PathVariable Long id) {
        return orgChartService.getOrgChart(id);
    }

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam String name) {
        return orgChartService.search(name);
    }


    @GetMapping("/roots")
    @PreAuthorize("hasAnyRole('Hr','Manager','Employee')")
    public List<UserDto> roots() {
        return orgChartService.getRoots();
    }

    @GetMapping("/user/{userId}/profile")
    public ProfileDto getProfile(@PathVariable Long userId) {

        return userService.getUserProfile(userId);
    }


}

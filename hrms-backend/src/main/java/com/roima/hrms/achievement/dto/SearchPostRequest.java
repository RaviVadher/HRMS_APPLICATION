package com.roima.hrms.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostRequest {

    private String author;
    private String tag;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String postType;
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortOrder;
}


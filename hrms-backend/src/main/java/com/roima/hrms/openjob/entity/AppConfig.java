package com.roima.hrms.openjob.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppConfig {

    @Id
    private String key;

    private String value;
}

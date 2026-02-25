package com.roima.hrms.openjob.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="appConfig")
public class AppConfig {

    @Id
    private String k;

    private String value;
}

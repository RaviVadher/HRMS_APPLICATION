package com.roima.hrms.travel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="required_documents")
public class RequiredDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_req_doc_id")
    private Long Id ;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name="fk_travel")
    private Travel travel;

    @NotBlank
    @Column(nullable = false)
    private String doc_name;

}

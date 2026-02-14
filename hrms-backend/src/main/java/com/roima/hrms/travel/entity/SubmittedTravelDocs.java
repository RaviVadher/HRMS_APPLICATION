package com.roima.hrms.travel.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Entity
@Table(name = "submited_travel_docs",uniqueConstraints = @UniqueConstraint(columnNames={"fk_assign_id","fk_doc_id"}))
public class SubmittedTravelDocs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_sub_id")
    private Long Id;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name="fk_assign")
    private TravelAssign travelAssign;

    @Column
    private String documentName;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_user_id")
    private User user;


    @NotBlank
    @Column(name="file_path")
    private String filepath;

    @NotBlank
    @Column(name = "file_type")
    private String filetype;

    @Column(nullable = false)
    private LocalDateTime upload_at=LocalDateTime.now();

}

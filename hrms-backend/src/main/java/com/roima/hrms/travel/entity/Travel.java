package com.roima.hrms.travel.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="travels")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_travel_id")
    private Long Id;

    @NotBlank
    private String travel_title;

    @NotBlank
    private String travel_description;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @Column(nullable = false)
    private LocalDate start_date;

    @Column(nullable = false)
    private LocalDate end_date;

    @ManyToOne(optional = false)
    @JoinColumn(name="fk_user_createdby")
    private User user;

    @Column(nullable = false)
    private LocalDate created_date;

    @OneToMany(mappedBy = "travel",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TravelAssign> assignList = new ArrayList<>();

    @AssertTrue(message = "end date must be after start date")
    public boolean isEndDateAfterStartDate() {

        return end_date!=null && start_date!=null && end_date.isAfter(start_date);
    }


}

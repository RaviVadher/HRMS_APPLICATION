package com.roima.hrms.travel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.results.graph.Fetch;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="travel_assigns", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_assign","fk_travel_assign"}))
public class TravelAssign {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pk_assign_id")
    private Long Id;

    LocalDate assignedDate;

    @ManyToOne(optional = false,fetch=FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "fk_user_assign")
    private User user;

    @ManyToOne(optional = false,fetch=FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "fk_travel_assign")
    private Travel travel;

    @JsonIgnore
    @OneToMany(mappedBy = "assign", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "travelAssign",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmittedTravelDocs> documents = new ArrayList<>();
}

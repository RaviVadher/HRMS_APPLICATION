package com.roima.hrms.gamescheduling.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="game_cycle")
@Getter
@Setter
public class GameCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_cycle_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id",nullable = false)
    private Game game;

    private Integer cycleNumber;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean active;
}

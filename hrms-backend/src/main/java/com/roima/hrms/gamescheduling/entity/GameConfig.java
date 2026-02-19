package com.roima.hrms.gamescheduling.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="game_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_gameconfig")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id",nullable = false,unique = true)
    private Game  game;

    private LocalTime startTime;
    private LocalTime endTime;

    @Column(nullable = false)
    private Integer slotDuration;

    @Column(nullable = false)
    private Integer maxPlayers;

    private String createdBy;
    private LocalDateTime createdAt;
}

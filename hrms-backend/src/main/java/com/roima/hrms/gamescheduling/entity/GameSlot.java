package com.roima.hrms.gamescheduling.entity;

import com.roima.hrms.gamescheduling.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="game_slot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_slot_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fk_game_id", nullable=false)
    private Game game;

    @Column(nullable = false)
    private LocalDate slotDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer bookedCount=0;

    @Enumerated(EnumType.STRING)
    private SlotStatus status= SlotStatus.Available;

}

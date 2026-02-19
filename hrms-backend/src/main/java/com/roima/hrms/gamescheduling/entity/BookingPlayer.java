package com.roima.hrms.gamescheduling.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="booking_players",uniqueConstraints = {@UniqueConstraint(name="unique_user_per_day",columnNames = {"user_id","slot_date"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_gameplayer")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booking_id",nullable = false)
    private Booking booking;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_gameslot_id",nullable = false)
    private GameSlot slot;

    private LocalDateTime joinedAt;

}

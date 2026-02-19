package com.roima.hrms.gamescheduling.entity;


import com.roima.hrms.gamescheduling.enums.BookingStatus;
import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="wait_queue")
@Getter
@Setter
public class WaitQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_q_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id",nullable = false)
    private Game game;

    private LocalDateTime requestAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status =BookingStatus.Waiting;
}

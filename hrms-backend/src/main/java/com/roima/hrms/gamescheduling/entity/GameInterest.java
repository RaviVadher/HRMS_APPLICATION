package com.roima.hrms.gamescheduling.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="game_interest",uniqueConstraints = {@UniqueConstraint(name="unique_user_game", columnNames = {"user_id","fk_game_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_interest")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_game_id",nullable = false)
    private Game game;


}

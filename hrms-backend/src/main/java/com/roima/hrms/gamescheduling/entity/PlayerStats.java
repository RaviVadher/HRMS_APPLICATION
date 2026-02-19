package com.roima.hrms.gamescheduling.entity;

import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="player_stats",  uniqueConstraints = {
        @UniqueConstraint(name="uniq_player_game",
                columnNames={"fk_user_id","fk_game_id"})
})
@Getter
@Setter
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_stat")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_game_id")
    private Game game;

    private Integer completedSlots = 0;
    private Integer currentCycleNo = 1;

    private LocalDateTime lastPlayedAt;
}

package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.BookingPlayer;
import com.roima.hrms.gamescheduling.entity.Game;
import com.roima.hrms.gamescheduling.entity.PlayerStats;
import com.roima.hrms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

    Optional <PlayerStats> findByUserIdAndGameId(Long userId, Long gameId);

    @Modifying
    @Query("update PlayerStats p set p.completedSlots= p.completedSlots+1 where p.game.id =:gameId And p.user.Id=:userId")
    void incComplatedSlot(Long gameId,Long userId);

    @Query("select  p from PlayerStats p where p.completedSlots=0 And p.game.id=:gameId")
    List<PlayerStats> countNotPlayed(Long gameId);

    @Modifying
    @Query("update PlayerStats p set p.completedSlots= 0 where p.game.id =:gameId")
    void resetCycle(Long gameId);

}

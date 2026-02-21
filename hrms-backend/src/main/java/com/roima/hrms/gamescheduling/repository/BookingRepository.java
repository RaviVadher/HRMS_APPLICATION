package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.dto.BookingHistoryResponseDto;
import com.roima.hrms.gamescheduling.entity.Booking;
import com.roima.hrms.gamescheduling.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {


    List<Booking> findBySlotIdAndStatus(Long slotId, BookingStatus bookingStatus);

    @Query(""" 
          select new com.roima.hrms.gamescheduling.dto.BookingHistoryResponseDto(
          s.id,b.id,s.startTime,s.endTime,b.bookedAt, b.status, u.name)
          from Booking b
          join b.slot s
          join BookingPlayer bp on bp.booking.id = b.id
          join User u on u.Id = b.bookedBy.Id
          where bp.user.Id = :userId and s.game.id = :gameId
          order by b.bookedAt desc
          """)
    List<BookingHistoryResponseDto> findByUserIdAndGameId(Long userId, Long gameId);
}

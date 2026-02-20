package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.BookingPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingPlayerRepository extends JpaRepository<BookingPlayer,Long> {

    List<BookingPlayer> findByBookingId(Long bookingId);
}

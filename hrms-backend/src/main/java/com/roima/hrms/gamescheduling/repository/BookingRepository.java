package com.roima.hrms.gamescheduling.repository;

import com.roima.hrms.gamescheduling.entity.Booking;
import com.roima.hrms.gamescheduling.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {


    List<Booking> findBySlotIdAndStatus(Long slotId, BookingStatus bookingStatus);
}

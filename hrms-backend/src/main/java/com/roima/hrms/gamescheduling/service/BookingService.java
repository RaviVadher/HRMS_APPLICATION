package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.gamescheduling.dto.BookingRequestDto;
import com.roima.hrms.gamescheduling.dto.BookingResponseDto;

public interface  BookingService {
    BookingResponseDto bookSlot(BookingRequestDto dto);
   void  processWaitingBookings();
   void  updateStatsAfterSlotEnd();
   void cancelSlot(Long bookedId);
}

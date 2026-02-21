package com.roima.hrms.gamescheduling.service;

import com.roima.hrms.gamescheduling.dto.BookingHistoryResponseDto;
import com.roima.hrms.gamescheduling.dto.BookingRequestDto;
import com.roima.hrms.gamescheduling.dto.BookingResponseDto;

import java.util.List;

public interface  BookingService {
    BookingResponseDto bookSlot(BookingRequestDto dto);
   void  processWaitingBookings();
   void  updateStatsAfterSlotEnd();
   void cancelSlot(Long bookedId);
   List<BookingHistoryResponseDto> findHistory(Long gameId, Long userId);
}

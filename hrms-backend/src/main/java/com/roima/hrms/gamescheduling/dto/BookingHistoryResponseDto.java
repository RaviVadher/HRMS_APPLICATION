package com.roima.hrms.gamescheduling.dto;

import com.roima.hrms.gamescheduling.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryResponseDto {

    private Long slotId;
    private Long bookingId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime bookingTime;
    private BookingStatus status;
    private String bookedBy;

}

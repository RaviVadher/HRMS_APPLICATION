package com.roima.hrms.gamescheduling.dto;

import com.roima.hrms.gamescheduling.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private Long bookingId;
    private String message;
    private BookingStatus status;
}

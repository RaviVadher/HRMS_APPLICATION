package com.roima.hrms.gamescheduling.entity;

import com.roima.hrms.gamescheduling.enums.BookingStatus;
import com.roima.hrms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name="booking",uniqueConstraints = {@UniqueConstraint(name="one_user_one_slot", columnNames = {"booked_by","fk_slot_id"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_slot_id",nullable = false)
    private GameSlot slot;

    @ManyToOne(optional = false)
    @JoinColumn(name="booked_by",nullable = false)
    private User bookedBy;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime bookedAt;

}

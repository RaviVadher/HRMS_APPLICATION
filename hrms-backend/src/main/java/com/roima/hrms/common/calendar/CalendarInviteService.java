package com.roima.hrms.common.calendar;

import com.roima.hrms.gamescheduling.entity.GameSlot;
import com.roima.hrms.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CalendarInviteService {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    private static final ZoneId ZONE = ZoneId.systemDefault();

    public String generateInvite(GameSlot slot, User organizer) {

        String uid = UUID.randomUUID().toString();

        ZonedDateTime start = ZonedDateTime.of(
                slot.getSlotDate(),
                slot.getStartTime(),
                ZONE
        );

        ZonedDateTime end = ZonedDateTime.of(
                slot.getSlotDate(),
                slot.getEndTime(),
                ZONE
        );

        return "BEGIN:VCALENDAR\n" +
                "PRODID:-//HRMS Game Scheduler//EN\n" +
                "VERSION:2.0\n" +
                "CALSCALE:GREGORIAN\n" +
                "METHOD:REQUEST\n" +
                "BEGIN:VEVENT\n" +
                "UID:" + uid + "\n" +
                "DTSTAMP:" + nowUTC() + "\n" +
                "DTSTART:" + formatUTC(start) + "\n" +
                "DTEND:" + formatUTC(end) + "\n" +
                "SUMMARY:" + slot.getGame().getGameName() + " Game Slot\n" +
                "DESCRIPTION:Game scheduled by " + organizer.getName() + "\n" +
                "LOCATION:Office Game Zone\n" +
                "STATUS:CONFIRMED\n" +
                "SEQUENCE:0\n" +
                "ORGANIZER;CN=" + organizer.getName() +
                ":mailto:" + organizer.getEmail() + "\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";
    }

    private String formatUTC(ZonedDateTime time) {
        return time.withZoneSameInstant(ZoneOffset.UTC).format(FORMAT) + "Z";
    }

    private String nowUTC() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(FORMAT) + "Z";
    }
}

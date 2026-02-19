package com.roima.hrms.gamescheduling.mapper;

import com.roima.hrms.gamescheduling.dto.SlotResponseDto;
import com.roima.hrms.gamescheduling.entity.GameSlot;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut;

public class SlotMapper {

    public static SlotResponseDto toDto(GameSlot slot) {
        SlotResponseDto dto = new SlotResponseDto();

        dto.setSlotId(slot.getId());
        dto.setDate(slot.getSlotDate());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        dto.setBookedCount(slot.getBookedCount());
        dto.setStatus(slot.getStatus());

        return dto;
    }
}

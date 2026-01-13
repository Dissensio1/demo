package com.example.demo.mapper;

import java.time.LocalDateTime;

import com.example.demo.dto.TimeEntryRequestDTO;
import com.example.demo.dto.TimeEntryResponseDTO;
import com.example.demo.model.TimeEntry;

public class TimeEntryMapper {
    public static TimeEntry timeEntryRequestToTimeEntry(TimeEntryRequestDTO request) {
        return new TimeEntry(null, request.student(), request.type(), request.subject(), "", LocalDateTime.now(), null, false);
    }

    public static TimeEntryResponseDTO timeEntryToTimeEntryResponseDTO(TimeEntry timeEntry) {
        return new TimeEntryResponseDTO(timeEntry.getId(), timeEntry.getStudent(), timeEntry.getType(),
                timeEntry.getSubject(), timeEntry.getDescription(), timeEntry.getTimestart(), timeEntry.getTimeend(), timeEntry.isBillable());
    }
}

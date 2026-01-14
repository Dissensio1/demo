package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.dto.TimeEntryRequestDTO;
import com.example.demo.dto.TimeEntryResponseDTO;
import com.example.demo.model.TimeEntry;

public class TimeEntryMapper {
    public static TimeEntry timeEntryRequestToTimeEntry(TimeEntryRequestDTO request) {
        return new TimeEntry(null, null, request.type(), request.subject(), "", LocalDateTime.now(), null, false);
    }

    public static TimeEntryResponseDTO timeEntryToTimeEntryResponseDTO(TimeEntry timeEntry) {
        return new TimeEntryResponseDTO(timeEntry.getId(), timeEntry.getStudent().getId(), timeEntry.getType(),
                timeEntry.getSubject(), timeEntry.getDescription(), timeEntry.getTimestart(), timeEntry.getTimeend(), timeEntry.isBillable());
    }
    public static List<TimeEntryResponseDTO> timeEntryToTimeEntryResponseDTOList(List<TimeEntry> timeEntries) {
        if (timeEntries == null) {
            return List.of();
        }
        return timeEntries.stream()
                .map(TimeEntryMapper::timeEntryToTimeEntryResponseDTO)
                .collect(Collectors.toList());
    }
}

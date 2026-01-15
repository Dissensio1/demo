package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.DeadlinePredictionDTO;
import com.example.demo.dto.DeadlineRequestDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.enums.RiskLevel;
import com.example.demo.model.Deadline;
import com.example.demo.model.Student;
import com.example.demo.repository.TimeEntryRepository;
import com.example.demo.service.TaskNormingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DeadlineMapper {
    private final TaskNormingService taskNormingService;
    private final TimeEntryRepository timeEntryRepository;

    public static Deadline deadlineRequestToDeadline(DeadlineRequestDTO request) {
        return new Deadline(null, request.subject(), request.type(), request.deadlineDate(), new HashSet<>());
    }

    public static DeadlineResponseDTO deadlineToDeadlineResponseDTO(Deadline deadline) {
        return new DeadlineResponseDTO(deadline.getId(), deadline.getSubject(), deadline.getType(),
                deadline.getDeadlineDate(), deadline.getStudents().stream().map(Student::getId).collect(Collectors.toSet()));
    }

    public DeadlinePredictionDTO deadlineToDeadlinePredictionDto(Deadline deadline, Long studentId) {
        double requiredHours = taskNormingService.getRequiredHours(deadline.getType());

        Double workedHours = timeEntryRepository.sumDurationByStudentAndSubjectAndType(
                studentId, deadline.getSubject(), deadline.getType());

        workedHours = Objects.requireNonNullElse(workedHours, 0.0);

        double hoursLeft = Math.max(0, requiredHours - workedHours);

        RiskLevel risk;
        double progressPercent = (workedHours / requiredHours) * 100;
        if (progressPercent < 50) {
            risk = RiskLevel.HIGH;
        } else if (progressPercent < 80) {
            risk = RiskLevel.MEDIUM;
        } else {
            risk = RiskLevel.LOW;
        }

        return new DeadlinePredictionDTO(deadline.getSubject(), deadline.getDeadlineDate(), hoursLeft, risk);
    }
}

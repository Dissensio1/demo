package com.example.demo.dto;

import java.time.LocalDate;
import java.util.Set;

import com.example.demo.enums.TaskType;

public record DeadlineResponseDTO(Long id, String subject, TaskType type, LocalDate deadlineDate, Set<Long> studentIds) {}

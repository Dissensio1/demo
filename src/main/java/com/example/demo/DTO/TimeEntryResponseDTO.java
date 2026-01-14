package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.enums.TaskType;

public record TimeEntryResponseDTO(Long id, Long studentId, TaskType type, String subject, String description, LocalDateTime timeStart, LocalDateTime timeEnd, boolean isBillable) {}

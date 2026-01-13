package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;

public record TimeEntryResponseDTO(Long id, Student student, TaskType type, String subject, String description, LocalDateTime timeStart, LocalDateTime timeEnd, boolean isBillable) {}

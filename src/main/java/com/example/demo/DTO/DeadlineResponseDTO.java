package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.enums.TaskType;

public record DeadlineResponseDTO(Long id, String subject, TaskType type, LocalDateTime deadlineDate) {}

package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.enums.TaskType;

public record DeadlineRequestDTO(String subject, LocalDate deadlineDate, TaskType type) {}

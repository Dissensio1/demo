package com.example.demo.dto;

import com.example.demo.enums.TaskType;

public record TimeEntryRequestDTO(Long studentId, TaskType type, String subject) {}

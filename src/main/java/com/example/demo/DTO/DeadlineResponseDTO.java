package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;

public record DeadlineResponseDTO(Long id, String subject, TaskType type, LocalDateTime deadlineDate, Set<Student> students) {}

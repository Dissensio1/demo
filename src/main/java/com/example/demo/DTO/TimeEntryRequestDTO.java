package com.example.demo.dto;

import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;

public record TimeEntryRequestDTO(Student student, TaskType type, String subject) {}

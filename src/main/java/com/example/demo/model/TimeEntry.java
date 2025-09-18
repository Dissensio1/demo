package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TimeEntry {
    private Long Id;
    private Student student;
    private TaskType type;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean isBillable; // учётное время
}

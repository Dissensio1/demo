package com.example.demo.service;

import org.springframework.stereotype.Component;

import com.example.demo.enums.TaskType;

@Component
public class TaskNormingService {
    public double getRequiredHours(TaskType type) {
        return switch (type) {
            case EXAM_PREP -> 60.0;
            case PROJECT -> 40.0;
            default -> 10.0;
        };
    }
}

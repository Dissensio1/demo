package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DeadlinePredictionDTO {
    private String subject;
    private LocalDateTime deadline;
    private Double hoursLeft;
    private RiskLevel risk; // LOW, MEDIUM, HIGH
}

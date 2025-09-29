package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.enums.RiskLevel;

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

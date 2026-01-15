package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.enums.RiskLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DeadlinePredictionDTO {
    private String subject;
    private LocalDate deadlineDate;
    private Double hoursLeft;
    private RiskLevel risk; // LOW, MEDIUM, HIGH
}

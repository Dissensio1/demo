package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.DeadlinePredictionDTO;
import com.example.demo.enums.RiskLevel;

import jakarta.validation.Valid;

@RequestMapping("/api")
@RestController
public class DeadlinePredictionDTOController {
    private List<DeadlinePredictionDTO> deadlines = new ArrayList<>(Arrays.asList(
        new DeadlinePredictionDTO("Programming",
        LocalDateTime.of(2025, 12, 31, 23, 59),
        1000.0, RiskLevel.LOW)
    ));

    @GetMapping("/deadlines")
    public List<DeadlinePredictionDTO> getDeadlines() {
        return deadlines;
    }

    @GetMapping("/deadlines/{risk}")
    public ResponseEntity<DeadlinePredictionDTO> getDeadline(@PathVariable RiskLevel risk) {
        for (DeadlinePredictionDTO deadline : deadlines) {
            if (deadline.getRisk().equals(risk)) {
                return ResponseEntity.ok(deadline);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/deadlines")
    public ResponseEntity<DeadlinePredictionDTO> addDeadline(@RequestBody @Valid DeadlinePredictionDTO deadline) {
        deadline.setRisk(RiskLevel.LOW);
        deadlines.add(deadline);
        return ResponseEntity.status(HttpStatus.CREATED).body(deadline);
    } 
}

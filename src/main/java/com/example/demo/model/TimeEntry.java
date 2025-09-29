package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    private Student student;
    @Size(min=2, max=100, message = "tasktype")
    @Column(nullable = false, length = 100)
    private TaskType type;
    @Size(min=2, max=100, message = "description")
    @Column(nullable = false, length = 100)
    private String description;
    @Size(min=2, max=100, message = "starttime")
    @Column(nullable = false, length = 100)
    private LocalDateTime start;
    @Size(min=2, max=100, message = "endtime")
    @Column(nullable = false, length = 100)
    private LocalDateTime end;
    @Size(min=2, max=100, message = "isbillable")
    @Column(nullable = false, length = 100)
    private boolean isBillable; // учётное время
}

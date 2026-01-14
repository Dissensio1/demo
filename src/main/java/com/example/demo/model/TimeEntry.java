package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.enums.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Size(min=2, max=100, message = "subject")
    @Column(nullable = false, length = 100)
    private String subject;

    @Size(min=2, max=100, message = "description")
    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false, length = 100)
    private LocalDateTime timestart;

    @Column(length = 100)
    private LocalDateTime timeend;

    @Column()
    private boolean isBillable; // учётное время
}

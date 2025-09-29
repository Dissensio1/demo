package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity 
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Size(min=2, max=100, message = "name")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @Size(min=2, max=100, message = "group")
    @Column(nullable = false, unique = true, length = 100)
    private String groupp;
    @ElementCollection
    private List<TimeEntry> recentEntries;
}

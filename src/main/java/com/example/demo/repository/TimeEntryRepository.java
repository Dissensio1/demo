package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.TaskType;
import com.example.demo.model.TimeEntry;

@Repository
public interface TimeEntryRepository extends JpaRepository <TimeEntry, Long>{
        List<TimeEntry> findAllByType(TaskType type);
    }

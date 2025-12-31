package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.TimeEntry;

public record StudentResponseDTO(Long id, String name, String group, List<TimeEntry> recentEntries) {}

package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.enums.TaskType;
import com.example.demo.model.TimeEntry;
import com.example.demo.repository.TimeEntryRepository;

@Service

public class TimeEntryService {
    private List<TimeEntry> timeEntries = new ArrayList<>();

    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }
    
    public TimeEntry create(TimeEntry timeEntry) {
        return timeEntryRepository.save(timeEntry);
    }

    public List<TimeEntry> getAll() {
        return timeEntryRepository.findAll();
    }
    
    public List<TimeEntry> getAllByType(TaskType type) {
        return timeEntryRepository.findAllByType(type);
    }
    
    public TimeEntry getById(Long id) {
        for (TimeEntry timeEntry : timeEntries) {
            if (timeEntry.getId().equals(id)) {
                return timeEntryRepository.findById(id).orElse(null);
            }
        }
        return null;
    }

    public TimeEntry update(Long id, TimeEntry timeEntry) {
        return timeEntryRepository.findById(id).map(existingTimeEntry -> {
            existingTimeEntry.setStart(timeEntry.getStart());
            existingTimeEntry.setEnd(timeEntry.getEnd());
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
    }

    public boolean deleteById(Long id){
        if(timeEntryRepository.existsById(id)){
            timeEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

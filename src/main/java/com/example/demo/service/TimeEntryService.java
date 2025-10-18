package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;
import com.example.demo.model.TimeEntry;
import com.example.demo.repository.TimeEntryRepository;
import com.example.demo.specifications.TimeEntrySpecification;

@Service
@Transactional(readOnly = true)
public class TimeEntryService {
    private List<TimeEntry> timeEntries = new ArrayList<>();

    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }
    
    @CacheEvict(value = "timeEntry", allEntries = true)
    @Transactional
    public TimeEntry create(TimeEntry timeEntry) {
        return timeEntryRepository.save(timeEntry);
    }

    @Cacheable(value = "timeEntries", key = "#root.methodName")
    public List<TimeEntry> getAll() {
        return timeEntryRepository.findAll();
    }
    
    public List<TimeEntry> getAllByType(TaskType type) {
        return timeEntryRepository.findAllByType(type);
    }
    
    @Cacheable(value = "timeEntry", key = "#id")
    public TimeEntry getById(Long id) {
        for (TimeEntry timeEntry : timeEntries) {
            if (timeEntry.getId().equals(id)) {
                return timeEntryRepository.findById(id).orElse(null);
            }
        }
        return null;
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "timeEntries", allEntries = true), @CacheEvict(value = "timeEntry", key = "#id")})
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        return timeEntryRepository.findById(id).map(existingTimeEntry -> {
            existingTimeEntry.setStart(timeEntry.getStart());
            existingTimeEntry.setEnd(timeEntry.getEnd());
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "timeEntries", allEntries = true), @CacheEvict(value = "timeEntry", key = "#id")})
    public boolean deleteById(Long id){
        if(timeEntryRepository.existsById(id)){
            timeEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<TimeEntry> getByFilter(Student student, TaskType type,
    LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd,
    boolean expression, Pageable pageable){
        return timeEntryRepository.findAll(TimeEntrySpecification.filter(type, student, dateTimeStart, dateTimeEnd, expression), pageable);
    }
}

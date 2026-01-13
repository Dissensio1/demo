package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TimeEntryRequestDTO;
import com.example.demo.dto.TimeEntryResponseDTO;
import com.example.demo.enums.TaskType;
import com.example.demo.mapper.TimeEntryMapper;
import com.example.demo.model.Student;
import com.example.demo.model.TimeEntry;
import com.example.demo.repository.TimeEntryRepository;
import com.example.demo.specifications.TimeEntrySpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeEntryService {
    private List<TimeEntry> timeEntries = new ArrayList<>();

    private final TimeEntryRepository timeEntryRepository;
    
    @CacheEvict(value = "timeEntry", allEntries = true)
    @Transactional
    public TimeEntryResponseDTO create(TimeEntryRequestDTO timeEntryRequestDTO) {
        TimeEntry timeEntry = timeEntryRepository.save(TimeEntryMapper.timeEntryRequestToTimeEntry(timeEntryRequestDTO));
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry);
    }

    @Cacheable(value = "timeEntries", key = "#root.methodName")
    public List<TimeEntryResponseDTO> getAll(){
        timeEntries = timeEntryRepository.findAll();
        List<TimeEntryResponseDTO> timeEntriesResponse = new ArrayList<>();
        for(TimeEntry timeEntry: timeEntries){
            timeEntriesResponse.add(TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry));
        }
        return timeEntriesResponse;
    }
    
    public List<TimeEntryResponseDTO> getAllByType(TaskType type) {
        timeEntries = timeEntryRepository.findAllByType(type);
        List<TimeEntryResponseDTO> timeEntriesResponse = new ArrayList<>();
        for (TimeEntry timeEntry : timeEntries) {
            timeEntriesResponse.add(TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry));
        }
        return timeEntriesResponse;
    }

    public List<TimeEntryResponseDTO> getAllByStudent(Student student) {
        timeEntries = timeEntryRepository.findAllByStudent(student);
        List<TimeEntryResponseDTO> timeEntriesResponse = new ArrayList<>();
        for (TimeEntry timeEntry : timeEntries) {
            timeEntriesResponse.add(TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry));
        }
        return timeEntriesResponse;
    }
    
    @Cacheable(value = "timeEntry", key = "#id")
    public TimeEntryResponseDTO getById(Long id) {
        TimeEntry timeEntry = timeEntryRepository.findById(id).orElse(null);
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "timeEntries", allEntries = true), @CacheEvict(value = "timeEntry", key = "#id")})
    public TimeEntryResponseDTO update(Long id, TimeEntryRequestDTO request) {
        TimeEntry timeEntry = timeEntryRepository.findById(id).map(existingTimeEntry -> {
            existingTimeEntry.setStudent(request.student());
            existingTimeEntry.setType(request.type());
            existingTimeEntry.setSubject(request.subject());
            return timeEntryRepository.save(existingTimeEntry);
        }).orElse(null);
        return TimeEntryMapper.timeEntryToTimeEntryResponseDTO(timeEntry);
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

    public Page<TimeEntry> getByFilter(String type, Student student, boolean expression, Pageable pageable) {
        return timeEntryRepository.findAll(TimeEntrySpecification.filter(type, student, expression), pageable);
    }
}

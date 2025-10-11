package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;
import com.example.demo.model.TimeEntry;
import com.example.demo.service.TimeEntryService;

import jakarta.validation.Valid;

@RequestMapping("/api/timeEntries")
@RestController
public class TimeEntryController{
    private final TimeEntryService timeEntryService;
    
    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }
    
    @PostMapping
    public ResponseEntity<TimeEntry> addTimeEntry(@RequestBody @Valid TimeEntry timeEntry) {
        TimeEntry newTimeEntry = timeEntryService.create(timeEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTimeEntry);
    }

    @GetMapping
    public List<TimeEntry> getTimeEntries(@RequestParam(required = false) TaskType type) {
        if(type == null) return timeEntryService.getAll();
        else return timeEntryService.getAllByType(type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> getTimeEntry(@PathVariable Long id) {
        return ResponseEntity.ok().body(timeEntryService.getById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
        @RequestParam(required = false)Student student,
        @RequestParam(required = false)TaskType type,
        @RequestParam(required = false)LocalDateTime dateTimeStart,
        @RequestParam(required = false)LocalDateTime dateTimeEnd,
        @RequestParam(required = false)boolean expression,
        @PageableDefault(page = 0, size = 10, sort = "title")Pageable pageable){
            return ResponseEntity.ok(timeEntryService.getByFilter(student, type, dateTimeStart, dateTimeEnd, expression, pageable));
        }
    

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntry> edit(@PathVariable Long id, @RequestBody TimeEntry timeEntry){
        TimeEntry updated = timeEntryService.update(id, timeEntry);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(timeEntryService.deleteById(id)){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}
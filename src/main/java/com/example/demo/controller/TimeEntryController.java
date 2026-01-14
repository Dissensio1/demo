package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TimeEntryRequestDTO;
import com.example.demo.dto.TimeEntryResponseDTO;
import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;
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
    public ResponseEntity<TimeEntryResponseDTO> addTimeEntry(@RequestBody @Valid TimeEntryRequestDTO request) {
        System.out.println("✅ Запрос дошёл до контроллера!");
        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryService.create(request));
    }

    @GetMapping("/type")
    public List<TimeEntryResponseDTO> getTimeEntriesByType(@RequestParam(required = false) TaskType type) {
        if(type == null) return timeEntryService.getAll();
        else return timeEntryService.getAllByType(type);
    }

    @GetMapping("/student")
    public List<TimeEntryResponseDTO> getTimeEntriesByStudent(@RequestParam(required = false) Student student) {
        if(student == null) return timeEntryService.getAll();
        else return timeEntryService.getAllByStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryResponseDTO> getTimeEntry(@PathVariable Long id) {
        return ResponseEntity.ok().body(timeEntryService.getById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
        @RequestParam(required = false)Long studentId,
        @RequestParam(required = false)String type,
        @RequestParam(required = false)boolean expression,
        @PageableDefault(page = 0, size = 10, sort = "title")Pageable pageable){
            return ResponseEntity.ok(timeEntryService.getByFilter(type, studentId, expression, pageable));
        }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable Long id, @RequestBody TimeEntryRequestDTO request){
        TimeEntryResponseDTO updated = timeEntryService.update(id, request);
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
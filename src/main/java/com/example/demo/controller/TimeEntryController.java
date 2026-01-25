package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.example.demo.service.TimeEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/timeEntries")
@RestController
@RequiredArgsConstructor
@Tag(name = "TimeEntries", description = "Methods for managing timeEntries")
public class TimeEntryController {
    private final TimeEntryService timeEntryService;

    @Operation(
        summary = "Create New TimeEntry",
        description = "Creates a new timeEntry in the system")
    @PostMapping
    public ResponseEntity<TimeEntryResponseDTO> addTimeEntry(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "TimeEntry data to create", required = true)
        @RequestBody @Valid TimeEntryRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryService.create(request));
    }

    @Operation(
        summary = "Get All TimeEntries",
        description = "Retrieves a list of all timeEntries in the system by task type if it's not null, else retrieves a list of all timeEntries")
    @GetMapping("/type")
    public List<TimeEntryResponseDTO> getTimeEntriesByType(
        @Parameter(description = "Task type of the timeEntry", required = true)
        @RequestParam(required = false) TaskType type) {
        if (type == null)
            return timeEntryService.getAll();
        else
            return timeEntryService.getAllByType(type);
    }

    @Operation(
        summary = "Get All TimeEntries",
        description = "Retrieves a list of all timeEntries in the system by studentId if it's not null, else retrieves a list of all timeEntries")
    @GetMapping("/student")
    public List<TimeEntryResponseDTO> getTimeEntriesByStudentId(
        @Parameter(description = "Id of the student", required = true)
        @RequestParam(required = false) Long id) {
        if (id == null)
            return timeEntryService.getAll();
        else
            return timeEntryService.getAllByStudentId(id);
    }

    @Operation(
        summary = "Get TimeEntry by ID",
        description = "Retrieves a specific timeEntry by its unique Id")
    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryResponseDTO> getTimeEntryById(
        @Parameter(description = "ID of the timeEntry to retrieve", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok().body(timeEntryService.getById(id));
    }

    @Operation(
        summary = "Filter TimeEntry",
        description = "Search and filter timeEntries")
    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
        @Parameter(description = "Task type of the timeEntry to filter by")
        @RequestParam(required = false)String type,
        @Parameter(description = "isBillable field of the timeEntry to filter by")
        @RequestParam(defaultValue = "false",required = false)Boolean isBillable,
        @Parameter(description = "The field by which the result is sorted, it can be type, isBillable or id")
        @RequestParam(defaultValue = "type") String sort){
        if (!List.of("type", "isBillable", "id").contains(sort)) {
            sort = "type";
        }
        Sort sortOrder = Sort.by(sort);
        Pageable pageable = PageRequest.of(0, 10, sortOrder);
        return ResponseEntity.ok(timeEntryService.getByFilter(type, isBillable, pageable));
    }

    @Operation(
        summary = "Update TimeEntry",
        description = "Updates an existing timeEntry")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> edit(
        @Parameter(description = "ID of the timeEntry to update", required = true)
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated timeEntry data", required = true)
        @RequestBody TimeEntryRequestDTO request) {
        TimeEntryResponseDTO updated = timeEntryService.update(id, request);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Delete TimeEntry",
        description = "Deletes a timeEntry from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID of the timeEntry to delete", required = true)
        @PathVariable Long id) {
        if (timeEntryService.deleteById(id)) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Set EndTime",
        description = "Sets the end date and time of a timeEntry")
    @PatchMapping("/setEndTime/{id}")
    public ResponseEntity<Object> setTimeEnd(
        @Parameter(description = "ID of the timeEntry to set endTime", required = true)
        @PathVariable Long id) {
        TimeEntryResponseDTO updated = timeEntryService.setTimeEnd(id);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }
}
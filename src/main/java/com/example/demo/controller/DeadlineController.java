package com.example.demo.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DeadlineRequestDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.service.DeadlineService;
import com.example.demo.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/deadlines")
@RequiredArgsConstructor
@Tag(name = "Deadlines", description = "Methods for managing deadlines")
public class DeadlineController {
    private final DeadlineService deadlineService;
    private final ReportService reportService;

    @Operation(
        summary = "Create New Deadline",
        description = "Creates a new deadline in the system")
    @PostMapping
    public ResponseEntity<DeadlineResponseDTO> addDeadline(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Deadline data to create", required = true)
        @RequestBody @Valid DeadlineRequestDTO deadlineReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deadlineService.create(deadlineReqDTO));
    }

    @Operation(
        summary = "Get All Deadlines",
        description = "Retrieves a list of all deadlines in the system by type if it's not null, else retrieves a list of all dealines")
    @GetMapping
    public List<DeadlineResponseDTO> getDeadlinesByType(@RequestParam(required = false) String type) {
        if(type == null) return deadlineService.getAll();
        else return deadlineService.getAllByType(type);
    }

    @Operation(
        summary = "Get Deadline by studentID",
        description = "Retrieves a specific deadline by studentId that applied to it")
    @GetMapping("/studentId")
    public List<DeadlineResponseDTO> getDeadlinesByStudentId(
        @Parameter(description = "ID of the student to retrieve deadline", required = true)
        @RequestParam(required = false) Long id) {
        if(id == null) return deadlineService.getAll();
        else return deadlineService.getAllByStudentId(id);
    }

    @Operation(
        summary = "Get Deadline by ID",
        description = "Retrieves a specific deadline by its unique Id")
    @GetMapping("/{id}")
    public ResponseEntity<DeadlineResponseDTO> getDeadlineById(
        @Parameter(description = "ID of the deadline to retrieve", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok().body(deadlineService.getById(id));
    }

    @Operation(
        summary = "Get Deadline Report",
        description = "Creates a report with all deadlines for a certain student")
    @GetMapping(value = "/getReport")
    public ResponseEntity<Resource> downloadDeadlinesForStudent(
    @Parameter(description = "ID of the student to create a report", required = true)
    @RequestParam(required = true) Long studentId) {
        Resource resource = reportService.getStudentDeadlinesReport(studentId);
        return ResponseEntity.ok()
        .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deadlines_report.xlsx")
        .body(resource);
    }

    @Operation(
        summary = "Update Deadline",
        description = "Updates an existing deadline")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> edit(
        @Parameter(description = "ID of the deadline to update", required = true)
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated deadline data", required = true)
        @RequestBody DeadlineRequestDTO deadlineReqDTO){
        DeadlineResponseDTO updated = deadlineService.update(id, deadlineReqDTO);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Delete Deadline",
        description = "Deletes a deadline from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID of the deadline to delete", required = true)
        @PathVariable Long id){
        if(deadlineService.deleteById(id)){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}

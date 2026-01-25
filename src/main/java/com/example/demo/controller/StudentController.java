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

import com.example.demo.dto.DeadlinePredictionDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.dto.StudentRequestDTO;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.service.DeadlineService;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Methods for managing students")
public class StudentController {
    private final StudentService studentService;
    private final DeadlineService deadlineService;
    
    @Operation(
        summary = "Create New Student",
        description = "Creates a new student in the system")
    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student data to create", required = true)
        @RequestBody @Valid StudentRequestDTO studentReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(studentReqDTO));
    }

    @Operation(
        summary = "Get All Students",
        description = "Retrieves a list of all students in the system by group if it's not null, else retrieves a list of all students")
    @GetMapping("/group")
    public List<StudentResponseDTO> getStudentsByGroup(
        @Parameter(description = "Group of the student", required = true)
        @RequestParam(required = false) String groupp) {
        if(groupp == null) return studentService.getAll();
        else return studentService.getAllByGroupp(groupp);
    }

    @Operation(
        summary = "Get Student by ID",
        description = "Retrieves a specific student by its unique Id")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(
        @Parameter(description = "ID of the student to retrieve", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.getById(id));
    }

    @Operation(
        summary = "Filter Student",
        description = "Search and filter students")
    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
        @Parameter(description = "Name of the student to filter by")
        @RequestParam(required = false)String name,
        @Parameter(description = "Group of the student to filter by")
        @RequestParam(required = false)String groupp,
        @Parameter(description = "The field by which the result is sorted, it can be name, groupp or id")
        @RequestParam(defaultValue = "name") String sort){
        if (!List.of("name", "groupp", "id").contains(sort)) {
            sort = "name";
        }
        Sort sortOrder = Sort.by(sort);
        Pageable pageable = PageRequest.of(0, 10, sortOrder);
        return ResponseEntity.ok(studentService.getByFilter(name, groupp, pageable));
    }
    

    @Operation(
        summary = "Update Student",
        description = "Updates an existing student")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> edit(
        @Parameter(description = "ID of the student to update", required = true)
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated student data", required = true)
        @RequestBody StudentRequestDTO studentReqDTO){
        StudentResponseDTO updated = studentService.update(id, studentReqDTO);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Delete Student",
        description = "Deletes a student from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID of the student to delete", required = true)
        @PathVariable Long id){
        if(studentService.deleteById(id)){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Apply deadline to student",
        description = "Applies a certain deadline to certain student")
    @PatchMapping("/addDeadlineTo/{studentId}")
    public ResponseEntity<Object> addDeadlineToStudent(
        @Parameter(description = "ID of the student to apply deadline", required = true)
        @PathVariable Long studentId,
        @Parameter(description = "ID of the deadline to aplly", required = true)
        @RequestParam Long deadlineId){
        DeadlineResponseDTO updated = deadlineService.addDeadlineToStudent(studentId, deadlineId);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Detach deadline to student",
        description = "Detaches a certain deadline from certain student")
    @PatchMapping("/removeDeadlineFrom/{studentId}")
    public ResponseEntity<Object> removeDeadlineFromStudent(
        @Parameter(description = "ID of the student from which the deadline is detached", required = true)
        @PathVariable Long studentId,
        @Parameter(description = "ID of the deadline to detach", required = true)
        @RequestParam Long deadlineId){
        DeadlineResponseDTO updated = deadlineService.removeDeadlineFromStudent(studentId, deadlineId);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Get Deadline Predicitons",
        description = "Gets deadline predictions for certain student")
    @GetMapping("/{studentId}/predictions")
    public List<DeadlinePredictionDTO> getPredictions(
        @Parameter(description = "ID of the student for whom deadline are predicted", required = true)
        @PathVariable Long studentId) {
        return deadlineService.getDeadlinePredictionsForStudent(studentId);
    }
}

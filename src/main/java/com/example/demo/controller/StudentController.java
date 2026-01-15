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

import com.example.demo.dto.DeadlinePredictionDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.dto.StudentRequestDTO;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.service.DeadlineService;
import com.example.demo.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final DeadlineService deadlineService;
    
    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(@RequestBody @Valid StudentRequestDTO studentReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(studentReqDTO));
    }

    @GetMapping
    public List<StudentResponseDTO> getStudents(@RequestParam(required = false) String groupp) {
        if(groupp == null) return studentService.getAll();
        else return studentService.getAllByGroupp(groupp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.getById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getByFilter(
        @RequestParam(required = false)String name,
        @RequestParam(required = false)String title,
        @PageableDefault(page = 0, size = 10, sort = "title")Pageable pageable){
            return ResponseEntity.ok(studentService.getByFilter(name, title, pageable));
        }
    

    @PatchMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable Long id, @RequestBody StudentRequestDTO studentReqDTO){
        StudentResponseDTO updated = studentService.update(id, studentReqDTO);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(studentService.deleteById(id)){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/addDeadlineTo/{studentId}")
    public ResponseEntity<Object> addDeadlineToStudent(@PathVariable Long studentId, @RequestParam Long deadlineId){
        DeadlineResponseDTO updated = deadlineService.addDeadlineToStudent(studentId, deadlineId);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/removeDeadlineFrom/{studentId}")
    public ResponseEntity<Object> removeDeadlineFromStudent(@PathVariable Long studentId, @RequestParam Long deadlineId){
        DeadlineResponseDTO updated = deadlineService.removeDeadlineFromStudent(studentId, deadlineId);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{studentId}/predictions")
    public List<DeadlinePredictionDTO> getPredictions(@PathVariable Long studentId) {
        return deadlineService.getDeadlinePredictionsForStudent(studentId);
    }
}

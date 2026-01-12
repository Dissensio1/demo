package com.example.demo.controller;

import java.util.List;

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

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/deadlines")
public class DeadlineController {
    private final DeadlineService deadlineService;

    public DeadlineController(DeadlineService deadlineService){
        this.deadlineService = deadlineService;
    }

    @PostMapping
    public ResponseEntity<DeadlineResponseDTO> addDeadline(@RequestBody @Valid DeadlineRequestDTO deadlineReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deadlineService.create(deadlineReqDTO));
    }

    @GetMapping
    public List<DeadlineResponseDTO> getDeadlines(@RequestParam(required = false) String type) {
        if(type == null) return deadlineService.getAll();
        else return deadlineService.getAllByType(type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeadlineResponseDTO> getDeadline(@PathVariable Long id) {
        return ResponseEntity.ok().body(deadlineService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> edit(@PathVariable Long id, @RequestBody DeadlineRequestDTO deadlineReqDTO){
        DeadlineResponseDTO updated = deadlineService.update(id, deadlineReqDTO);
        if(updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(deadlineService.deleteById(id)){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}

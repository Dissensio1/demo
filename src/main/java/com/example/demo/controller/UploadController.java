package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.UploadResponseDTO;
import com.example.demo.service.UploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    @PostMapping(value = "/students", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadStudents(@RequestParam MultipartFile file) {
        UploadResponseDTO response = uploadService.importStudents(file);
        if (response.failureCount() > 0) return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/deadlines", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadDeadlines(@RequestParam MultipartFile file) {
        UploadResponseDTO response = uploadService.importDeadlines(file);
        if (response.failureCount() > 0) return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        return ResponseEntity.ok(response);
    }
}

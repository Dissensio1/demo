package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.DeadlineRequestDTO;
import com.example.demo.dto.StudentRequestDTO;
import com.example.demo.dto.UploadResponseDTO;
import com.example.demo.enums.TaskType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final StudentService studentService;
    private final DeadlineService deadlineService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadLocation;

    private void validateFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("The system supports only CSV files");
        }

    }

    public UploadResponseDTO importDeadlines(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errorList = new ArrayList<>();

        try {validateFile(file);}
        catch (IllegalArgumentException e) {
            errorList.add(file.getOriginalFilename() + " : " + e.getMessage());
            throw new IllegalArgumentException("File validation failed");
        }

        CSVFormat format =
        CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).get();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                CSVParser csvParser = format.parse(reader)) {
                for (CSVRecord csvrecord : csvParser) {
                    try {
                        DeadlineRequestDTO request = new DeadlineRequestDTO(
                            csvrecord.get("subject"),
                            LocalDate.parse(csvrecord.get("deadlineDate")),
                            TaskType.valueOf(csvrecord.get("type").toUpperCase())
                        );

                        deadlineService.create(request);
                        successCount++;
                    }
                    catch (Exception e) {
                        failureCount++;
                    }
                }
            return new UploadResponseDTO(successCount + failureCount,
                successCount,
                failureCount,
                errorList
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage(), e);
        }
    }

    public UploadResponseDTO importStudents(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errorList = new ArrayList<>();

        try {validateFile(file);}
        catch (IllegalArgumentException e) {
            errorList.add(file.getOriginalFilename() + " : " + e.getMessage());
            throw new IllegalArgumentException("File validation failed");
        }
        
        CSVFormat format =
        CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).get();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                CSVParser csvParser = format.parse(reader)) {
                for (CSVRecord csvrecord : csvParser) {
                    try {
                        StudentRequestDTO request = new StudentRequestDTO(
                            csvrecord.get("name"),
                            csvrecord.get("groupp")
                        );

                        studentService.create(request);
                        successCount++;
                    }
                    catch (Exception e) {
                        failureCount++;
                    }
                }
            return new UploadResponseDTO(successCount + failureCount,
                successCount,
                failureCount,
                errorList
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage(), e);
        }
    }
}

package com.example.demo.dto;

import java.util.List;

public record UploadResponseDTO(int totalRows, int successCount, int failureCount, List<String> errorList) {}

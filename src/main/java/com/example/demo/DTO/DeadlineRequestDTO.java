package com.example.demo.dto;

import java.time.LocalDateTime;

public record DeadlineRequestDTO(String subject, LocalDateTime deadlineDate) {}

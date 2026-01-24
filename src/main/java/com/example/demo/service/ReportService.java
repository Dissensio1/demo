package com.example.demo.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DeadlineReportDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.dto.StudentResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final StudentService studentService;
    private final DeadlineService deadlineService;

    @Value("${report.template-location}")
    private Resource templateLocation;

    public ByteArrayResource getStudentDeadlinesReport(Long studentId) {
        StudentResponseDTO student = studentService.getById(studentId);
        List<DeadlineResponseDTO> deadlines = deadlineService.getAllByStudentId(studentId);

        List<DeadlineReportDTO> deadlineReports = deadlines.stream()
            .map(d -> new DeadlineReportDTO(
                d.subject(),
                d.type().name(),
                d.deadlineDate().toString()
            ))
            .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("student", student);
        data.put("deadlines", deadlineReports);

        try(InputStream is = templateLocation.getInputStream()) {
            byte[] reportBytes = JxlsPoiTemplateFillerBuilder.newInstance()
                    .withTemplate(is)
                    .buildAndFill(data);

            logger.info("Successfully generated deadlines report for student ID: {}", studentId);
            return new ByteArrayResource(reportBytes);
        }catch (Exception e) {
            logger.error("Error generating report for student ID: {}", studentId, e);
            throw new RuntimeException("Failed to generate student deadlines report", e);
        }
    }
}

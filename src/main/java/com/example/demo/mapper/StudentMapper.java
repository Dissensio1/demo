package com.example.demo.mapper;

import java.util.ArrayList;
import java.util.HashSet;

import com.example.demo.dto.StudentRequestDTO;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.model.Student;

public class StudentMapper {
    public static Student studentRequestToStudent(StudentRequestDTO request) {
        return new Student(null, request.name(), request.groupp(), new ArrayList<>(), new HashSet<>());
    }

    public static StudentResponseDTO studentToStudentResponseDTO(Student student) {
        return new StudentResponseDTO(student.getId(), student.getName(), student.getGroupp(),
                student.getRecentEntries(), student.getDeadlines());
    }
}

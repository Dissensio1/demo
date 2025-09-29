package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service

public class StudentService {
    private List<Student> students = new ArrayList<>();

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    
    public List<Student> getAllByTitle(String title) {
        return studentRepository.findByNameStartingWithIgnoreCase(title);
    }
    
    public Student getById(Long id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return studentRepository.findById(id).orElse(null);
            }
        }
        return null;
    }

    public Student update(Long id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setGroupp(student.getGroupp());
            return studentRepository.save(existingStudent);
        }).orElse(null);
    }

    public boolean deleteById(Long id){
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

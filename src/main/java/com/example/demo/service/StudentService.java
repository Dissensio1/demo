package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.StudentRequestDTO;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.specifications.StudentSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
    private List<Student> students = new ArrayList<>();

    private final StudentRepository studentRepository;

    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request) {
        Student student = studentRepository.save(StudentMapper.studentRequestToStudent(request));
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Cacheable(value = "students", key = "#root.methodName")
    public List<StudentResponseDTO> getAll() {
        students = studentRepository.findAll();
        List<StudentResponseDTO> studentsResponse = new ArrayList<>();
        for (Student student : students) {
            studentsResponse.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        return studentsResponse;
    }
    
    public List<StudentResponseDTO> getAllByGroupp(String groupp) {
        students = studentRepository.findAllByGroupp(groupp);
        List<StudentResponseDTO> studentsResponse = new ArrayList<>();
        for (Student student : students) {
            studentsResponse.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        return studentsResponse;
    }
    
    @Cacheable(value = "student", key = "#id")
    public StudentResponseDTO getById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "students", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public StudentResponseDTO update(Long id, StudentRequestDTO request) {
        Student student = studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(request.name());
            existingStudent.setGroupp(request.groupp());
            return studentRepository.save(existingStudent);
        }).orElse(null);
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "students", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public boolean deleteById(Long id){
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Student> getByFilter(String name, String groupp, Pageable pageable) {
        return studentRepository.findAll(StudentSpecifications.filter(name, groupp), pageable);
    }
}

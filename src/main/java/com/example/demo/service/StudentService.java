package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.StudentRequestDTO;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.specifications.StudentSpecifications;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
    private List<Student> students = new ArrayList<>();

    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @CacheEvict(value = "student", allEntries = true)
    @Transactional
    public StudentResponseDTO create(StudentRequestDTO request) {
        logger.info("Creating new Student: {} group: {}", request.name(), request.groupp());
        Student student = studentRepository.save(StudentMapper.studentRequestToStudent(request));
        logger.info("Successfully created Student with ID: {} name: {}", student.getId(), student.getName());
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Cacheable(value = "students", key = "#root.methodName")
    public List<StudentResponseDTO> getAll() {
        students = studentRepository.findAll();
        List<StudentResponseDTO> studentsResponse = new ArrayList<>();
        for (Student student : students) {
            studentsResponse.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        logger.info("Successfully retrieved all Students. Total count: {}", studentsResponse.size());
        return studentsResponse;
    }
    
    public List<StudentResponseDTO> getAllByGroupp(String groupp) {
        students = studentRepository.findAllByGroupp(groupp);
        List<StudentResponseDTO> studentsResponse = new ArrayList<>();
        for (Student student : students) {
            studentsResponse.add(StudentMapper.studentToStudentResponseDTO(student));
        }
        logger.info("Successfully retrieved all Students by group. Total count: {}", studentsResponse.size());
        return studentsResponse;
    }
    
    @Cacheable(value = "student", key = "#id")
    public StudentResponseDTO getById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if(student != null){
            logger.info("Successfully retrieved Student by id: {}.", student.getId());
            return StudentMapper.studentToStudentResponseDTO(student);
        }
        throw new EntityNotFoundException("There's no Student with ID: " + id.toString());
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "students", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public StudentResponseDTO update(Long id, StudentRequestDTO request) {
        Student student = studentRepository.findById(id).map(existingStudent -> {
            logger.debug("Values before update - name: {}, group: {}", existingStudent.getName(), existingStudent.getGroupp());
            existingStudent.setName(request.name());
            existingStudent.setGroupp(request.groupp());
            logger.info("Successfully updated Student with ID: {}", id);
            return studentRepository.save(existingStudent);
        }).orElse(null);
        if (student == null) {
            throw new EntityNotFoundException("There's no Student with ID: " + id.toString());
        }
        return StudentMapper.studentToStudentResponseDTO(student);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "students", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public boolean deleteById(Long id){
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            logger.info("Successfully deleted Student with ID: {}", id);
            return true;
        }
        throw new EntityNotFoundException("There's no Student with ID: " + id.toString());
    }

    public Page<StudentResponseDTO> getByFilter(String name, String groupp, Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(
            StudentSpecifications.filter(name, groupp), 
            pageable
        );

        List<StudentResponseDTO> dtoList = studentPage.getContent().stream()
            .map(StudentMapper::studentToStudentResponseDTO)
            .toList();

        Page<StudentResponseDTO> result = new PageImpl<>(
            dtoList,
            pageable,
            studentPage.getTotalElements()
        );

        logger.info("Successfully filtered Students. Found {} results", result.getNumberOfElements());
        return result;
    }
}

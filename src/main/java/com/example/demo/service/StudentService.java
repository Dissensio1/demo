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
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Cacheable(value = "students", key = "#root.methodName")
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    
    public List<Student> getAllByTitle(String title) {
        return studentRepository.findByNameStartingWithIgnoreCase(title);
    }
    
    @Cacheable(value = "student", key = "#id")
    public Student getById(Long id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return studentRepository.findById(id).orElse(null);
            }
        }
        return null;
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "students", allEntries = true), @CacheEvict(value = "student", key = "#id")})
    public Student update(Long id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setGroupp(student.getGroupp());
            return studentRepository.save(existingStudent);
        }).orElse(null);
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

    public Page<Student> getByFilter(String name, String title, Pageable pageable){
        return studentRepository.findAll(StudentSpecifications.filter(name, title), pageable);
    }
}

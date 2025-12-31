package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Student;
import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository <Student, Long>, JpaSpecificationExecutor<Student>{
        List<Student> findByNameStartingWithIgnoreCase(String name);
        List<Student> findAllByGroupp(String groupp);
    }

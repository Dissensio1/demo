package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Deadline;

@Repository
public interface DeadlineRepository extends JpaRepository<Deadline, Long>, JpaSpecificationExecutor<Deadline>{
    List<Deadline> findAllByType(String type);
}

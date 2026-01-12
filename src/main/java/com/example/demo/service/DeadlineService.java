package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.DeadlineRequestDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.dto.StudentResponseDTO;
import com.example.demo.mapper.DeadlineMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Deadline;
import com.example.demo.model.Student;
import com.example.demo.repository.DeadlineRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeadlineService {
    private List<Deadline> deadlines = new ArrayList<>();

    private final DeadlineRepository deadlineRepository;

    public DeadlineResponseDTO create(DeadlineRequestDTO request){
        Deadline deadline = deadlineRepository.save(DeadlineMapper.deadlineRequestToDeadline(request));
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    public List<DeadlineResponseDTO> getAll(){
        deadlines = deadlineRepository.findAll();
        List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
        for(Deadline deadline: deadlines){
            deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
        }
        return deadlinesResponse;
    }

    public List<DeadlineResponseDTO> getAllByType(String type) {
        deadlines = deadlineRepository.findAllByType(type);
        List<DeadlineResponseDTO> deadlinesResponse = new ArrayList<>();
        for (Deadline deadline : deadlines) {
            deadlinesResponse.add(DeadlineMapper.deadlineToDeadlineResponseDTO(deadline));
        }
        return deadlinesResponse;
    }

    @Cacheable(value = "deadline", key = "#id")
    public DeadlineResponseDTO getById(Long id) {
        Deadline deadline = deadlineRepository.findById(id).orElse(null);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "deadlines", allEntries = true), @CacheEvict(value = "deadline", key = "#id")})
    public DeadlineResponseDTO update(Long id, DeadlineRequestDTO request) {
        Deadline deadline = deadlineRepository.findById(id).map(existingDeadline -> {
            existingDeadline.setSubject(request.subject());
            existingDeadline.setDeadlineDate(request.deadlineDate());
            return deadlineRepository.save(existingDeadline);
        }).orElse(null);
        return DeadlineMapper.deadlineToDeadlineResponseDTO(deadline);
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = "deadlines", allEntries = true), @CacheEvict(value = "deadline", key = "#id")})
    public boolean deleteById(Long id){
        if(deadlineRepository.existsById(id)){
            deadlineRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

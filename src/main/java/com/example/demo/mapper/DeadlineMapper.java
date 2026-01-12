package com.example.demo.mapper;

import com.example.demo.dto.DeadlineRequestDTO;
import com.example.demo.dto.DeadlineResponseDTO;
import com.example.demo.model.Deadline;

public class DeadlineMapper {
    public static Deadline deadlineRequestToDeadline(DeadlineRequestDTO request){
        return new Deadline(null, request.subject(), null, request.deadlineDate());
    }

    public static DeadlineResponseDTO deadlineToDeadlineResponseDTO(Deadline deadline){
        return new DeadlineResponseDTO(deadline.getId(), deadline.getSubject(), deadline.getType(), deadline.getDeadlineDate());
    }
}

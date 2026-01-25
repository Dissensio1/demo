package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.enums.TaskType;
import com.example.demo.model.TimeEntry;

public class TimeEntrySpecification {
    private static Specification<TimeEntry> typeLike(String type){
        return (root, query, criteriaBuilder) ->{
            if(type == null) {return null;}
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<TimeEntry> isBillableLike(boolean isBillable){
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.equal(root.get("isBillable"), isBillable);
        };
    }

    public static Specification<TimeEntry> filter(String type, boolean isBillable){
        return Specification.allOf(
            typeLike(type),
            isBillableLike(isBillable)
        );
    }
}

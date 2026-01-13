package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.Student;
import com.example.demo.model.TimeEntry;

public class TimeEntrySpecification {
    private static Specification<TimeEntry> studentLike(Student student){
        return (root, query, criteriaBuilder) ->{
            if(student==null){return null;}
            return criteriaBuilder.equal(root.get("student"), student);
        };
    }

    private static Specification<TimeEntry> typeLike(String type){
        return (root, query, criteriaBuilder) ->{
            if(type==null){return null;}
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<TimeEntry> isBillableLike(boolean expr){
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.equal(root.get("isBillable"), expr);
        };
    }

    public static Specification<TimeEntry> filter(String type, Student student, boolean expression){
        return Specification.allOf(
            typeLike(type),
            studentLike(student),
            isBillableLike(expression)
        );
    }
}

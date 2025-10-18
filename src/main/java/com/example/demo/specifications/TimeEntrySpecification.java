package com.example.demo.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.enums.TaskType;
import com.example.demo.model.Student;
import com.example.demo.model.TimeEntry;

public class TimeEntrySpecification {
    private static Specification<TimeEntry> studentLike(Student student){
        return (root, query, criteriaBuilder) ->{
            if(student==null){return null;}
            return criteriaBuilder.equal(root.get("student"), student);
        };
    }

    private static Specification<TimeEntry> typeLike(TaskType type){
        return (root, query, criteriaBuilder) ->{
            if(type==null){return null;}
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<TimeEntry> timeDateStartLike(LocalDateTime timeDate){
        return (root, query, criteriaBuilder) ->{
            if(timeDate==null){return null;}
            return criteriaBuilder.equal(root.get("start"), timeDate);
        };
    }

    private static Specification<TimeEntry> timeDateEndLike(LocalDateTime timeDate){
        return (root, query, criteriaBuilder) ->{
            if(timeDate==null){return null;}
            return criteriaBuilder.equal(root.get("end"), timeDate);
        };
    }

    private static Specification<TimeEntry> isBillableLike(boolean expr){
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.equal(root.get("isBillable"), expr);
        };
    }

    /* На всякий */
    // private static Specification<Student> priceGreater(Integer min){
    //     return (root, query, criteriaBuilder) ->{
    //         if(min==null){return null;}
    //         return criteriaBuilder.greaterThanOrEqualTo(
    //             root.get("price"), min);
    //     };
    // }

    // private static Specification<Student> priceLess(Integer max){
    //     return (root, query, criteriaBuilder) ->{
    //         if(max==null){return null;}
    //         return criteriaBuilder.lessThanOrEqualTo(
    //             root.get("price"), max);
    //     };
    // }

    public static Specification<TimeEntry> filter(TaskType type, Student student,
    LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, boolean expression){
        return Specification.allOf(
            typeLike(type),
            studentLike(student),
            timeDateStartLike(dateTimeStart),
            timeDateEndLike(dateTimeEnd),
            isBillableLike(expression)
        );
    }
}

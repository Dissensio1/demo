package com.example.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.Student;

public class StudentSpecifications {
    private static Specification<Student> nameLike(String name){
        return (root, query, criteriaBuilder) ->{
            if(name == null || name.trim().isEmpty()) {return null;}
            return criteriaBuilder.like(criteriaBuilder.lower(
                root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Student> grouppLike(String groupp){
        return (root, query, criteriaBuilder) ->{
            if(groupp == null || groupp.trim().isEmpty()){return null;}
            return criteriaBuilder.like(criteriaBuilder.lower(
                root.get("groupp")), "%" + groupp.trim().toLowerCase() + "%");
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

    public static Specification<Student> filter(String name, String title){
        return Specification.allOf(
            nameLike(name),
            grouppLike(title)
        );
    }
}

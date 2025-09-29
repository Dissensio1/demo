package com.example.demo.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    private Long Id;
    private String title;
    private Set<Permission> permissions;
}

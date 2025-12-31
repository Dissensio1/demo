package com.example.demo.model;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Permission implements GrantedAuthority{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min=2, max=100, message = "name")
    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private String operation;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    @Override
    public String getAuthority(){
        return String.format("%s:%s", resource.toUpperCase(), operation.toUpperCase());
    }
}

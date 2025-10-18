package com.example.demo.dto;

public record UserDTO(Long id, String username, String password, String role, Set<String> permissions) implements Serializable{}

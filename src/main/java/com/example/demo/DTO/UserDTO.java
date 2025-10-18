package com.example.demo.dto;

import java.io.Serializable;
import java.util.Set;

public record UserDTO(Long Id, 
String username, 
String password, 
String role, 
Set<String> permissions) implements Serializable{}

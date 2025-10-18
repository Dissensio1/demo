package com.example.demo.dto;

import java.util.Set;

public record UserLoggedDTO(String username, String role, Set<String> permissions) {

}

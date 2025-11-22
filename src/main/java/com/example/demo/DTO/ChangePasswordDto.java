package com.example.demo.dto;

import java.io.Serializable;

public record ChangePasswordDto(String oldPassword, String newPassword, String repeatedNewPassword)
        implements Serializable {}

package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserLoggedDTO;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@CookieValue(name = "access_token", required = false) String access,
            @CookieValue(name = "refresh_token", required = false) String refresh, @RequestBody LoginRequest request) {
        return authService.login(request, access, refresh);
    }

    @PatchMapping("/password/change")
    public ResponseEntity<LoginResponse> changePassword(
            @CookieValue(name = "access_token", required = false) String access,
            @CookieValue(name = "refresh_token", required = false) String refresh,
            @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
        return authService.changePassword(changePasswordDto, access, refresh);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refresh_token", required = false) String refresh) {
        return authService.refresh(refresh);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
            @CookieValue(name = "access_token", required = false) String access,
            @CookieValue(name = "refresh_token", required = false) String refresh) {
        return authService.logout(access, refresh);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public ResponseEntity<UserLoggedDTO> userLoggedInfo() {
        return ResponseEntity.ok(authService.info());
    }
}

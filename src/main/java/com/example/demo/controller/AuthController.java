package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserLoggedDTO;
import com.example.demo.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Methods for user authentication and authorization")
public class AuthController {
    public final AuthService authService;

    @Operation(
        summary = "Login User",
        description = "Authenticates user credentials and returns JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@CookieValue(name = "access_token", required = false) String access,
            @CookieValue(name = "refresh_token", required = false) String refresh, @RequestBody LoginRequest request) {
        return authService.login(request, access, refresh);
    }

    @Operation(
        summary = "Change User password",
        description = "Changes User password to new one and logs him out after it")
    @PatchMapping("/password/change")
    public ResponseEntity<LoginResponse> changePassword(
            @CookieValue(name = "access_token", required = false) String access,
            @CookieValue(name = "refresh_token", required = false) String refresh,
            @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
        return authService.changePassword(changePasswordDto, access, refresh);
    }

    @Operation(
        summary = "Refresh User tokens",
        description = "Checks User tokens and refreshes the if needed")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refresh_token", required = false) String refresh) {
        return authService.refresh(refresh);
    }

    @Operation(
        summary = "Logout User",
        description = "Revokes User tokens and logs him out")
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
            @CookieValue(name = "access_token", required = false) String access,
            @CookieValue(name = "refresh_token", required = false) String refresh) {
        return authService.logout(access, refresh);
    }

    @Operation(
        summary = "Get User info",
        description = "Shows info about authorized User")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public ResponseEntity<UserLoggedDTO> userLoggedInfo() {
        return ResponseEntity.ok(authService.info());
    }
}

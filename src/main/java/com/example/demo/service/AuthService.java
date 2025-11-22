package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserLoggedDTO;
import com.example.demo.jwt.JwtTokenProvider;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Token;
import com.example.demo.model.User;
import com.example.demo.repository.TokenRepository;
import com.example.demo.utils.CookieUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    public final UserService userService;
    public final TokenRepository tokenRepository;
    public final CookieUtil cookieUtil;
    public final JwtTokenProvider jwtTokenProvider;
    public final AuthenticationManager authenticationManager;
    public final PasswordEncoder passwordEncoder;

    @Value("${jwt.access.duration.minutes}")
    private long accessDurationMin;
    @Value("${jwt.access.duration.seconds}")
    private long accessDurationSec;
    @Value("${jwt.refresh.duration.days}")
    private long refreshDurationDay;
    @Value("${jwt.refresh.duration.seconds}")
    private long refreshDurationSec;

    private void addAccessTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessCookie(token.getValue(), accessDurationSec).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createResfreshCookie(token.getValue(), refreshDurationSec).toString());
    }

    private void revokeAllTokens(User user) {
        Set<Token> tokens = user.getTokens();
        tokens.forEach(token -> {
            if (token.getExpiringDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(token);
            } else if (!token.isDisabled()) {
                token.setDisabled(true);
                tokenRepository.save(token);
            }
        });
    }

    public ResponseEntity<LoginResponse> login(LoginRequest req, String access, String refresh) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        User user = userService.getUser(req.username());

        boolean accessValid = jwtTokenProvider.isValid(access);
        boolean refreshValid = jwtTokenProvider.isValid(refresh);
        
        HttpHeaders headers = new HttpHeaders();
        revokeAllTokens(user);

        if (!accessValid) {
            Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()),
                    accessDurationMin, ChronoUnit.MINUTES, user);
            newAccess.setUser(user);
            addAccessTokenCookie(headers, newAccess);
            tokenRepository.save(newAccess);
        }
        if (!refreshValid) {
            Token newRefresh = jwtTokenProvider.generateRefreshToken(refreshDurationDay, ChronoUnit.DAYS, user);
            newRefresh.setUser(user);
            addRefreshTokenCookie(headers, newRefresh);
            tokenRepository.save(newRefresh);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse(true, user.getUsername(), user.getRole().getName());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    public ResponseEntity<LoginResponse> refresh(String refreshToken) {
        if (!jwtTokenProvider.isValid(refreshToken)) {
            throw new RuntimeException("token is invalid");
        }
        User user = userService.getUser(jwtTokenProvider.getUsername(refreshToken));
        Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()), accessDurationMin, ChronoUnit.MINUTES, user);
        HttpHeaders headers = new HttpHeaders();
        addAccessTokenCookie(headers, newAccess);
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(true, user.getUsername(), user.getRole().getName()));
    }

    public ResponseEntity<LoginResponse> logout(String access, String refresh) {
        SecurityContextHolder.clearContext();
        User user = userService.getUser(jwtTokenProvider.getUsername(refresh));
        revokeAllTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessCookie().toString());
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshCookie().toString());
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(false, null,  null));
    }

    public UserLoggedDTO info() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("No user");
        }

        User user = userService.getUser(auth.getName());
        return UserMapper.userToUserLoggedDto(user);
    }

    public ResponseEntity<LoginResponse> changePassword(ChangePasswordDto changePasswordDto, String access, String refresh) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());

        if (!passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword())){
            throw new RuntimeException("Incorrectly entered old password");
        }

        if(!changePasswordDto.newPassword().equals(changePasswordDto.repeatedNewPassword())){
            throw new RuntimeException("The new entered passwords aren't similar");
        }

        if(passwordEncoder.matches(changePasswordDto.oldPassword(), changePasswordDto.newPassword())){
            throw new RuntimeException("The new password is incorrect");
        }

        userService.updatePassword(auth.getName(), changePasswordDto.newPassword());
        return logout(access, refresh);
    }
}

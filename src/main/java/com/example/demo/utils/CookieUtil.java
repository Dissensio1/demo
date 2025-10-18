package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${jwt.access.cookie_name}")
    private String accessTokenCookieName;

    @Value("${jwt.refresh.cookie_name}")
    private String refreshTokenCookieName;

    public HttpCookie createAccessCookie(String value, long duration){
        return ResponseCookie.from(accessTokenCookieName, value)
        .maxAge(duration).httpOnly(true).secure(true)
        .path("/").sameSite("None").build();
    }

    public HttpCookie deleteAccessCookie(){
        return ResponseCookie.from(accessTokenCookieName, "")
        .maxAge(0).httpOnly(true).secure(true)
        .path("/").sameSite("None").build();
    }

    public HttpCookie createResfreshCookie(String value, long duration){
        return ResponseCookie.from(refreshTokenCookieName, value)
        .maxAge(duration).httpOnly(true).secure(true)
        .path("/").sameSite("None").build();
    }

    public HttpCookie deleteResfreshCookie(){
        return ResponseCookie.from(refreshTokenCookieName, "")
        .maxAge(0).httpOnly(true).secure(true)
        .path("/").sameSite("None").build();
    }
}

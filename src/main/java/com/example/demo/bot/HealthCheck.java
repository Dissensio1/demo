package com.example.demo.bot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.BotService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HealthCheck {
    private final BotService botService;

    @Scheduled(cron = "0 39 15 * * *")
    public void sendHealthCheck () {
        botService.sendToAdmin("Ok");
    }
}

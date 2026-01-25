package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {        
        return new OpenAPI()
            .info(new Info()
                .title("Student Time Management API")
                .description("REST API for time management. " +
                           "This API allows you to track your work as a student " +
                           "and monitor your deadlines.")
                .version("i dunno"));
    }
}

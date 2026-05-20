package com.DragonForge.character_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
    // Apunta al microservicio de dados (rng-service) que corre localmente
        return builder.baseUrl("http://localhost:8080").build();
    }
}
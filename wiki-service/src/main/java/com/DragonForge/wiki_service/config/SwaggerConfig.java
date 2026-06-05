package com.DragonForge.wiki_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DragonForge - Wiki Service API")
                        .version("1.0")
                        .description("Microservicio de enciclopedia y lore. Almacena la historia del mundo, facciones, deidades y permite la interacción mediante comentarios y artículos creados por los Dungeon Masters.")
                );
    }
}
package com.DragonForge.character_service.config;

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
                        .title("DragonForge - Character Service API")
                        .version("1.0")
                        .description("Microservicio central para la gestión de personajes de Dungeons & Dragons. Controla estadísticas, puntos de golpe, razas, clases y el inventario equipado de cada jugador.")
                );
    }
}
package com.DragonForge.compendium_service.config;

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
                        .title("DragonForge - Compendium Service API")
                        .version("1.0")
                        .description("Microservicio sagrado que contiene el Bestiario, los hechizos y las reglas del universo. Permite la consulta de estadísticas de monstruos, efectos mágicos y lore técnico.")
                );
    }
}
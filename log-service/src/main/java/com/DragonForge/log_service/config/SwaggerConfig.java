package com.DragonForge.log_service.config;

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
                        .title("DragonForge - Log Service API")
                        .version("1.0")
                        .description("Microservicio encargado del Diario de Aventuras y Registro de Combate. Almacena el historial de eventos importantes, ataques y decisiones de cada campaña.")
                );
    }
}
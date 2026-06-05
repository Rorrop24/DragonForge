package com.DragonForge.rng_service.config;

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
                        .title("DragonForge - RNG Service API")
                        .version("1.0")
                        .description("Microservicio matemático responsable de las tiradas de dados virtuales." +
                                " Gestiona la aleatoriedad y los cálculos de éxito crítico o fallo para las campañas.")
                );
    }
}
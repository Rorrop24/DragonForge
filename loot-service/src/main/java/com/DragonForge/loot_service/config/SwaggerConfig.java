package com.DragonForge.loot_service.config;

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
                        .title("DragonForge - Loot Service API")
                        .version("1.0")
                        .description("Microservicio encargado del inventario global. Gestiona la creación, edición y consulta de armas, armaduras, pociones y objetos mágicos de toda la plataforma.")
                );
    }
}
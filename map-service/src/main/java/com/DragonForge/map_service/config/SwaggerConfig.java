package com.DragonForge.map_service.config;

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
                        .title("DragonForge - Map Service API")
                        .version("1.0")
                        .description("Microservicio cartográfico. Gestiona los mapas, regiones del mundo y las distintas ubicaciones y niveles de peligro para las campañas de Dungeons & Dragons.")
                );
    }
}
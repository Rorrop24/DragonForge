package com.DragonForge.user_service.config;

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
                        .title("DragonForge - User Service API")
                        .version("1.0")
                        .description("Microservicio núcleo para la gestión de identidades. Administra el registro, las cuentas, y los perfiles de los Jugadores y Dungeon Masters de la plataforma.")
                );
    }
}
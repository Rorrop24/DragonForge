package com.DragonForge.notification_service.config;

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
                        .title("DragonForge - Notification Service API")
                        .version("1.0")
                        .description("Microservicio de mensajería y alertas. Gestiona los buzones de los jugadores y el envío de notificaciones sobre turnos, daño recibido o mensajes del Dungeon Master.")
                );
    }
}
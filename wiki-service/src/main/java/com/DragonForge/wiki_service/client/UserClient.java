package com.DragonForge.wiki_service.client;

import com.DragonForge.wiki_service.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserClient {

    @Autowired
    private WebClient webClient;

    public UsuarioDTO obtenerUsuarioPorId(Integer id) {
        try {
            return webClient.get()
                    .uri("/api/v1/usuarios/{id}", id)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error: No se encontró al usuario con ID " + id + " en el user-service.");
        }
    }
}
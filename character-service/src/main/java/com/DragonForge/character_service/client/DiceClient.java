package com.DragonForge.character_service.client;

import com.DragonForge.character_service.dto.RollResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DiceClient {

    @Autowired
    private WebClient webClient;

    public RollResultDTO tirarDadosDeVida() {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/dice/roll")
                            .queryParam("sides", 10)
                            .queryParam("count", 1)
                            .queryParam("modifier", 5)
                            .build())
                    .retrieve()
                    .bodyToMono(RollResultDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Advertencia: No se pudo conectar al rng-service.");
            return null;
        }
    }
}
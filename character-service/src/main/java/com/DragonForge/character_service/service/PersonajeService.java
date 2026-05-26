package com.DragonForge.character_service.service;

import com.DragonForge.character_service.dto.PersonajeDTO;
import com.DragonForge.character_service.dto.RollResultDTO;
import com.DragonForge.character_service.model.Personaje;
import com.DragonForge.character_service.repository.PersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonajeService {

    @Autowired
    private PersonajeRepository repository;

    @Autowired
    private WebClient webClient;

    public PersonajeDTO crearPersonaje(Personaje personaje) {
        RollResultDTO resultadoDados = null;

        try {
            resultadoDados = webClient.get()
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
        }

        if (resultadoDados != null) {
            personaje.setPuntosGolpe(resultadoDados.getTotal());
        } else {
            personaje.setPuntosGolpe(15);
        }

        Personaje guardado = repository.save(personaje);
        return convertToDTO(guardado);
    }

    public List<PersonajeDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PersonajeDTO buscarPorId(Integer id) {
        Personaje p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personaje no encontrado"));
        return convertToDTO(p);
    }

    private PersonajeDTO convertToDTO(Personaje personaje) {
        PersonajeDTO dto = new PersonajeDTO();
        dto.setId(personaje.getId());
        dto.setNombrePersonaje(personaje.getNombrePersonaje());
        dto.setRaza(personaje.getRaza());
        dto.setClase(personaje.getClase());
        dto.setNivel(personaje.getNivel());
        dto.setPuntosGolpe(personaje.getPuntosGolpe());
        dto.setTrasfondo(personaje.getTrasfondo());
        dto.setVivo(personaje.getVivo());
        return dto;
    }
}
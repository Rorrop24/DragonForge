package com.DragonForge.character_service.service;

import com.DragonForge.character_service.client.DiceClient;
import com.DragonForge.character_service.dto.PersonajeDTO;
import com.DragonForge.character_service.dto.RollResultDTO;
import com.DragonForge.character_service.model.Personaje;
import com.DragonForge.character_service.repository.PersonajeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonajeService {

    @Autowired
    private PersonajeRepository repository;

    @Autowired
    private DiceClient diceClient;

    public PersonajeDTO crearPersonaje(Personaje personaje) {

        log.info("Iniciando la creación del personaje: {}", personaje.getNombrePersonaje());

        RollResultDTO resultadoDados = diceClient.tirarDadosDeVida();

        if (resultadoDados != null) {
            personaje.setPuntosGolpe(resultadoDados.getTotal());
            log.info("Dados de vida obtenidos exitosamente desde rng-service: {}", resultadoDados.getTotal());
        } else {
            personaje.setPuntosGolpe(15);
            log.warn("El rng-service no respondió. Asignando 15 puntos de golpe por defecto al personaje: {}", personaje.getNombrePersonaje());
        }

        // Guardamos en la base de datos
        Personaje guardado = repository.save(personaje);

        log.info("Personaje '{}' creado y guardado en la base de datos con ID: {}", guardado.getNombrePersonaje(), guardado.getId());
        return convertToDTO(guardado);
    }

    public List<PersonajeDTO> listarTodos() {
        log.info("Consultando la lista completa de personajes en la base de datos...");

        List<Personaje> personajes = repository.findAll();

        log.info("Se encontraron {} personajes registrados.", personajes.size());

        return personajes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PersonajeDTO buscarPorId(Integer id) {
        log.info("Buscando personaje con ID: {}", id);

        Personaje p = repository.findById(id)
                .orElseThrow(() -> {
                    // LOG DE ERROR: Queda registro exacto si alguien busca un ID fantasma
                    log.error("Error en la búsqueda: No se encontró ningún personaje con el ID {}", id);
                    return new RuntimeException("Personaje no encontrado");
                });

        log.info("Personaje encontrado exitosamente: {}", p.getNombrePersonaje());
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
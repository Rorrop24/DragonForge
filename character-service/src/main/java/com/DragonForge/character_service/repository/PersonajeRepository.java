package com.DragonForge.character_service.repository;

import com.DragonForge.character_service.model.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonajeRepository
    extends JpaRepository<Personaje, Integer>{
}

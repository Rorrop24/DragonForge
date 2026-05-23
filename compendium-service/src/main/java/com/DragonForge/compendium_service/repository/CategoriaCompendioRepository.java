package com.DragonForge.compendium_service.repository;

import com.DragonForge.compendium_service.model.CategoriaCompendio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaCompendioRepository extends JpaRepository<CategoriaCompendio, Integer> {
}
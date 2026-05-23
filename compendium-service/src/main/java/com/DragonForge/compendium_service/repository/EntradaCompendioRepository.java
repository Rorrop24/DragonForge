package com.DragonForge.compendium_service.repository;

import com.DragonForge.compendium_service.model.EntradaCompendio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntradaCompendioRepository extends JpaRepository<EntradaCompendio, Integer> {
    List<EntradaCompendio> findByCategoriaId(Integer categoriaId);
}
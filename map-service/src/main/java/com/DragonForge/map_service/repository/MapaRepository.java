package com.DragonForge.map_service.repository;

import com.DragonForge.map_service.model.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapaRepository extends JpaRepository<Mapa, Integer> {
}
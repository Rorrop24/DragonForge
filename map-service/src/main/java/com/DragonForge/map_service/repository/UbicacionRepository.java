package com.DragonForge.map_service.repository;

import com.DragonForge.map_service.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    List<Ubicacion> findByMapaId(Integer mapaId);
}
package com.DragonForge.asset_service.repository;

import com.DragonForge.asset_service.model.ArchivoActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArchivoActivoRepository extends JpaRepository<ArchivoActivo, Integer> {
    List<ArchivoActivo> findByCarpetaId(Integer carpetaId);
}
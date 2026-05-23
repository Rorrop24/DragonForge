package com.DragonForge.asset_service.repository;

import com.DragonForge.asset_service.model.CarpetaActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarpetaActivoRepository extends JpaRepository<CarpetaActivo, Integer> {
}
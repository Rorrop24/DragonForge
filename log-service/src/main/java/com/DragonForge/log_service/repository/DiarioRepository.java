package com.DragonForge.log_service.repository;

import com.DragonForge.log_service.model.Diario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiarioRepository extends JpaRepository<Diario, Integer> {
}
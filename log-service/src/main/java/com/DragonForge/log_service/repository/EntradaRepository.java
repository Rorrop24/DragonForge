package com.DragonForge.log_service.repository;

import com.DragonForge.log_service.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Integer> {
    List<Entrada> findByDiarioId(Integer diarioId);
}
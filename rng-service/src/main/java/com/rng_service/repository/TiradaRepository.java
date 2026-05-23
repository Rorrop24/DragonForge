package com.rng_service.repository;

import com.DragonForge.rng_service.model.Tirada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiradaRepository extends JpaRepository<Tirada, Integer> {
}
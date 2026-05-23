package com.DragonForge.notification_service.repository;

import com.DragonForge.notification_service.model.Buzon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BuzonRepository extends JpaRepository<Buzon, Integer> {
    Optional<Buzon> findByUsuarioId(Integer usuarioId);
}
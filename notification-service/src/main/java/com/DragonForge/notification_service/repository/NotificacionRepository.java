package com.DragonForge.notification_service.repository;

import com.DragonForge.notification_service.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByBuzonId(Integer buzonId);
    List<Notificacion> findByBuzonIdAndLeidaFalse(Integer buzonId);
}
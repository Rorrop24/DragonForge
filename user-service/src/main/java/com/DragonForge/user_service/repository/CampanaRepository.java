package com.DragonForge.user_service.repository;

import com.DragonForge.user_service.model.Campana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampanaRepository extends JpaRepository<Campana, Integer> {
    List<Campana> findByDungeonMasterId(Integer dungeonMasterId);
}
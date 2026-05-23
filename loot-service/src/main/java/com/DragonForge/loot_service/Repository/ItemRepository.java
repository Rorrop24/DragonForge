package com.DragonForge.loot_service.repository;

import com.DragonForge.loot_service.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCategoriaId(Integer categoriaId);
}
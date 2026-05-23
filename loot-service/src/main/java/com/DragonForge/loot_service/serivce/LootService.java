package com.DragonForge.loot_service.service;

import com.DragonForge.loot_service.model.Categoria;
import com.DragonForge.loot_service.model.Item;
import com.DragonForge.loot_service.repository.CategoriaRepository;
import com.DragonForge.loot_service.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LootService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Item> listarTodosLosItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> buscarItemPorId(Integer id) {
        return itemRepository.findById(id);
    }

    public List<Item> buscarItemsPorCategoria(Integer categoriaId) {
        return itemRepository.findByCategoriaId(categoriaId);
    }

    public Item guardarItem(Item item) {
        Integer catId = item.getCategoria().getId();
        Categoria categoriaExiste = categoriaRepository.findById(catId)
                .orElseThrow(() -> new RuntimeException("La categoría con ID " + catId + " no existe."));

        item.setCategoria(categoriaExiste);
        return itemRepository.save(item);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
}
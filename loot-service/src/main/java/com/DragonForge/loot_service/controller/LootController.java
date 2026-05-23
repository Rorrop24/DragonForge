package com.DragonForge.loot_service.controller;

import com.DragonForge.loot_service.model.Categoria;
import com.DragonForge.loot_service.model.Item;
import com.DragonForge.loot_service.service.LootService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/loot")
public class LootController {

    @Autowired
    private LootService lootService;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> listarItems() {
        List<Item> items = lootService.listarTodosLosItems();
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> buscarItemPorId(@PathVariable Integer id) {
        Optional<Item> item = lootService.buscarItemPorId(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/items/categoria/{categoriaId}")
    public ResponseEntity<List<Item>> listarPorCategoria(@PathVariable Integer categoriaId) {
        List<Item> items = lootService.buscarItemsPorCategoria(categoriaId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @PostMapping("/items")
    public ResponseEntity<?> crearItem(@Valid @RequestBody Item item) {
        try {
            Item nuevoItem = lootService.guardarItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = lootService.listarCategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }
}
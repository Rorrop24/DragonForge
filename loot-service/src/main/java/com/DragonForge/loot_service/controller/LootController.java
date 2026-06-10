package com.DragonForge.loot_service.controller;

import com.DragonForge.loot_service.model.Categoria;
import com.DragonForge.loot_service.model.Item;
import com.DragonForge.loot_service.service.LootService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/loot")
@Tag(name = "Inventario y Botín", description = "Operaciones para gestionar armas, armaduras, pociones y otras categorías de objetos mágicos o mundanos")
public class LootController {

    @Autowired
    private LootService lootService;

    @Operation(summary = "Obtiene el catálogo completo de todos los objetos y botín disponibles en el juego")
    @GetMapping("/items")
    public ResponseEntity<List<Item>> listarItems() {
        List<Item> items = lootService.listarTodosLosItems();
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Busca los detalles, rareza y estadísticas de un objeto específico mediante su ID")
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> buscarItemPorId(@PathVariable Integer id) {
        Optional<Item> item = lootService.buscarItemPorId(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filtra y lista todos los objetos que pertenecen a una categoría específica (ej: solo espadas o solo pociones)")
    @GetMapping("/items/categoria/{categoriaId}")
    public ResponseEntity<List<Item>> listarPorCategoria(@PathVariable Integer categoriaId) {
        List<Item> items = lootService.buscarItemsPorCategoria(categoriaId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "Forja un nuevo objeto asignándole sus estadísticas y vinculándolo a una categoría existente")
    @PostMapping("/items")
    public ResponseEntity<?> crearItem(@Valid @RequestBody Item item) {
        try {
            Item nuevoItem = lootService.guardarItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtiene el listado de todas las categorías en las que se clasifica el botín")
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = lootService.listarCategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }
    @Operation(summary = "Personaliza un objeto existente")
    @PutMapping("/items/{id}")
    public ResponseEntity<?> actualizarItem(@PathVariable Integer id, @Valid @RequestBody Item item) {
        try {
            return ResponseEntity.ok(lootService.actualizarItem(id, item));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un objeto mediante su ID")
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Integer id) {
        try {
            lootService.eliminarItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Busca una categoria de botin mediante su ID")
    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> buscarCategoria(@PathVariable Integer id) {
        Optional<Categoria> categoria = lootService.buscarCategoriaPorId(id);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crea una categoria de botin")
    @PostMapping("/categorias")
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lootService.crearCategoria(categoria));
    }

    @Operation(summary = "Personaliza una categoria de botin")
    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody Categoria categoria) {
        try {
            return ResponseEntity.ok(lootService.actualizarCategoria(id, categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una categoria de botin")
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        try {
            lootService.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

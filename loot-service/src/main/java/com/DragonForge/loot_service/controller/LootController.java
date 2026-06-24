package com.DragonForge.loot_service.controller;

import com.DragonForge.loot_service.model.Categoria;
import com.DragonForge.loot_service.model.Item;
import com.DragonForge.loot_service.service.LootService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/loot")
@Tag(name = "Inventario y Botin", description = "Operaciones para gestionar armas, armaduras, pociones y otras categorias de objetos magicos o mundanos")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class LootController {

    @Autowired
    private LootService lootService;

    @Operation(summary = "Obtiene el catalogo completo de todos los objetos y botin disponibles en el juego")
    @GetMapping("/items")
    public ResponseEntity<CollectionModel<Item>> listarItems() {
        List<Item> items = lootService.listarTodosLosItems();
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Item> model = CollectionModel.of(items);
        model.add(linkTo(methodOn(LootController.class).listarItems()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Busca los detalles, rareza y estadisticas de un objeto especifico mediante su ID")
    @GetMapping("/items/{id}")
    public ResponseEntity<EntityModel<Item>> buscarItemPorId(@PathVariable Integer id) {
        Optional<Item> item = lootService.buscarItemPorId(id);
        return item.map(value -> {
                    EntityModel<Item> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(LootController.class).buscarItemPorId(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filtra y lista todos los objetos que pertenecen a una categoria especifica (ej: solo espadas o solo pociones)")
    @GetMapping("/items/categoria/{categoriaId}")
    public ResponseEntity<CollectionModel<Item>> listarPorCategoria(@PathVariable Integer categoriaId) {
        List<Item> items = lootService.buscarItemsPorCategoria(categoriaId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Item> model = CollectionModel.of(items);
        model.add(linkTo(methodOn(LootController.class).listarPorCategoria(categoriaId)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Forja un nuevo objeto asignandole sus estadisticas y vinculandolo a una categoria existente")
    @PostMapping("/items")
    public ResponseEntity<?> crearItem(@Valid @RequestBody Item item) {
        try {
            Item nuevoItem = lootService.guardarItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtiene el listado de todas las categorias en las que se clasifica el botin")
    @GetMapping("/categorias")
    public ResponseEntity<CollectionModel<Categoria>> listarCategorias() {
        List<Categoria> categorias = lootService.listarCategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Categoria> model = CollectionModel.of(categorias);
        model.add(linkTo(methodOn(LootController.class).listarCategorias()).withSelfRel());
        return ResponseEntity.ok(model);
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
    public ResponseEntity<EntityModel<Categoria>> buscarCategoria(@PathVariable Integer id) {
        Optional<Categoria> categoria = lootService.buscarCategoriaPorId(id);
        return categoria.map(value -> {
                    EntityModel<Categoria> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(LootController.class).buscarCategoria(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
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
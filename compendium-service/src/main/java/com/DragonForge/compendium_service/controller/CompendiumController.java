package com.DragonForge.compendium_service.controller;

import com.DragonForge.compendium_service.model.CategoriaCompendio;
import com.DragonForge.compendium_service.model.EntradaCompendio;
import com.DragonForge.compendium_service.service.CompendiumService;
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
@RequestMapping("/api/v1/compendium")
@Tag(name = "Compendio y Bestiario", description = "Operaciones para gestionar el conocimiento del mundo: categorías, monstruos, hechizos y reglas de D&D")
public class CompendiumController {

    @Autowired
    private CompendiumService compendiumService;

    @Operation(summary = "Obtiene el listado de todas las categorías del compendio (ej: Monstruos, Hechizos, Objetos)")
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaCompendio>> listarCategorias() {
        List<CategoriaCompendio> categorias = compendiumService.listarCategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Crea una nueva categoría maestra para organizar el conocimiento")
    @PostMapping("/categorias")
    public ResponseEntity<CategoriaCompendio> crearCategoria(@Valid @RequestBody CategoriaCompendio categoria) {
        CategoriaCompendio nuevaCategoria = compendiumService.crearCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @Operation(summary = "Obtiene el listado completo de todas las entradas (todo el lore y bestiario mezclado)")
    @GetMapping("/entradas")
    public ResponseEntity<List<EntradaCompendio>> listarTodasLasEntradas() {
        List<EntradaCompendio> entradas = compendiumService.listarTodasLasEntradas();
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @Operation(summary = "Busca los detalles completos de una entrada específica (ej: estadísticas de un Dragón) mediante su ID")
    @GetMapping("/entradas/{id}")
    public ResponseEntity<EntradaCompendio> buscarEntrada(@PathVariable Integer id) {
        Optional<EntradaCompendio> entrada = compendiumService.buscarEntradaPorId(id);
        return entrada.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filtra y lista únicamente las entradas que pertenecen a una categoría específica")
    @GetMapping("/categorias/{id}/entradas")
    public ResponseEntity<List<EntradaCompendio>> buscarPorCategoria(@PathVariable Integer id) {
        List<EntradaCompendio> entradas = compendiumService.buscarEntradasPorCategoria(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @Operation(summary = "Registra una nueva entrada (monstruo, regla, hechizo) asociándola directamente a una categoría")
    @PostMapping("/categorias/{id}/entradas")
    public ResponseEntity<?> agregarEntrada(@PathVariable Integer id, @Valid @RequestBody EntradaCompendio entrada) {
        try {
            EntradaCompendio nuevaEntrada = compendiumService.agregarEntrada(id, entrada);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEntrada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
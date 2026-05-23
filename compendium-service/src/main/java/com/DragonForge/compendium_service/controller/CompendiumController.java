package com.DragonForge.compendium_service.controller;

import com.DragonForge.compendium_service.model.CategoriaCompendio;
import com.DragonForge.compendium_service.model.EntradaCompendio;
import com.DragonForge.compendium_service.service.CompendiumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/compendium")
public class CompendiumController {

    @Autowired
    private CompendiumService compendiumService;

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaCompendio>> listarCategorias() {
        List<CategoriaCompendio> categorias = compendiumService.listarCategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    @PostMapping("/categorias")
    public ResponseEntity<CategoriaCompendio> crearCategoria(@Valid @RequestBody CategoriaCompendio categoria) {
        CategoriaCompendio nuevaCategoria = compendiumService.crearCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @GetMapping("/entradas")
    public ResponseEntity<List<EntradaCompendio>> listarTodasLasEntradas() {
        List<EntradaCompendio> entradas = compendiumService.listarTodasLasEntradas();
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @GetMapping("/entradas/{id}")
    public ResponseEntity<EntradaCompendio> buscarEntrada(@PathVariable Integer id) {
        Optional<EntradaCompendio> entrada = compendiumService.buscarEntradaPorId(id);
        return entrada.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/categorias/{id}/entradas")
    public ResponseEntity<List<EntradaCompendio>> buscarPorCategoria(@PathVariable Integer id) {
        List<EntradaCompendio> entradas = compendiumService.buscarEntradasPorCategoria(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

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
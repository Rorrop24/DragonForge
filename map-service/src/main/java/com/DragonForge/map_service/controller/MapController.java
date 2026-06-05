package com.DragonForge.map_service.controller;

import com.DragonForge.map_service.model.Mapa;
import com.DragonForge.map_service.model.Ubicacion;
import com.DragonForge.map_service.service.MapService;
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
@RequestMapping("/api/v1/maps")
@Tag(name = "Cartografía y Mapas", description = "Operaciones para gestionar mapas del mundo, mazmorras y puntos de interés geográfico")
public class MapController {

    @Autowired
    private MapService mapService;

    @Operation(summary = "Obtiene el catálogo completo de todos los mapas creados para las campañas")
    @GetMapping
    public ResponseEntity<List<Mapa>> listarMapas() {
        List<Mapa> mapas = mapService.listarMapas();
        if (mapas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mapas);
    }

    @Operation(summary = "Busca la información detallada de un mapa específico mediante su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Mapa> buscarMapa(@PathVariable Integer id) {
        Optional<Mapa> mapa = mapService.buscarMapa(id);
        return mapa.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crea un nuevo mapa general (ej: Continente central o nivel 1 de una mazmorra)")
    @PostMapping
    public ResponseEntity<Mapa> crearMapa(@Valid @RequestBody Mapa mapa) {
        Mapa nuevoMapa = mapService.crearMapa(mapa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMapa);
    }

    @Operation(summary = "Lista todos los puntos de interés (ciudades, tabernas, trampas) vinculados a un mapa específico")
    @GetMapping("/{id}/ubicaciones")
    public ResponseEntity<List<Ubicacion>> listarUbicaciones(@PathVariable Integer id) {
        List<Ubicacion> ubicaciones = mapService.listarUbicacionesDeMapa(id);
        if (ubicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ubicaciones);
    }

    @Operation(summary = "Registra una nueva ubicación o punto de interés dentro de un mapa existente")
    @PostMapping("/{id}/ubicaciones")
    public ResponseEntity<?> agregarUbicacion(@PathVariable Integer id, @Valid @RequestBody Ubicacion ubicacion) {
        try {
            Ubicacion nuevaUbicacion = mapService.agregarUbicacion(id, ubicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUbicacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
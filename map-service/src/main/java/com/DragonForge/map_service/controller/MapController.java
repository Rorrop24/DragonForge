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
    @Operation(summary = "Personaliza un mapa existente")
    @PutMapping("/{id}")
    public ResponseEntity<Mapa> actualizarMapa(@PathVariable Integer id, @Valid @RequestBody Mapa mapa) {
        try {
            return ResponseEntity.ok(mapService.actualizarMapa(id, mapa));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un mapa y sus ubicaciones")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMapa(@PathVariable Integer id) {
        try {
            mapService.eliminarMapa(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Busca una ubicacion mediante su ID")
    @GetMapping("/ubicaciones/{id}")
    public ResponseEntity<Ubicacion> buscarUbicacion(@PathVariable Integer id) {
        Optional<Ubicacion> ubicacion = mapService.buscarUbicacion(id);
        return ubicacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Personaliza una ubicacion existente")
    @PutMapping("/ubicaciones/{id}")
    public ResponseEntity<?> actualizarUbicacion(@PathVariable Integer id, @Valid @RequestBody Ubicacion ubicacion) {
        try {
            return ResponseEntity.ok(mapService.actualizarUbicacion(id, ubicacion));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una ubicacion mediante su ID")
    @DeleteMapping("/ubicaciones/{id}")
    public ResponseEntity<Void> eliminarUbicacion(@PathVariable Integer id) {
        try {
            mapService.eliminarUbicacion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

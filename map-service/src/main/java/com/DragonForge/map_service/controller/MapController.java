package com.DragonForge.map_service.controller;

import com.DragonForge.map_service.model.Mapa;
import com.DragonForge.map_service.model.Ubicacion;
import com.DragonForge.map_service.service.MapService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/maps")
public class MapController {

    @Autowired
    private MapService mapService;

    @GetMapping
    public ResponseEntity<List<Mapa>> listarMapas() {
        List<Mapa> mapas = mapService.listarMapas();
        if (mapas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mapas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mapa> buscarMapa(@PathVariable Integer id) {
        Optional<Mapa> mapa = mapService.buscarMapa(id);
        return mapa.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mapa> crearMapa(@Valid @RequestBody Mapa mapa) {
        Mapa nuevoMapa = mapService.crearMapa(mapa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMapa);
    }

    @GetMapping("/{id}/ubicaciones")
    public ResponseEntity<List<Ubicacion>> listarUbicaciones(@PathVariable Integer id) {
        List<Ubicacion> ubicaciones = mapService.listarUbicacionesDeMapa(id);
        if (ubicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ubicaciones);
    }

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
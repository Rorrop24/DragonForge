package com.DragonForge.map_service.controller;

import com.DragonForge.map_service.model.Mapa;
import com.DragonForge.map_service.model.Ubicacion;
import com.DragonForge.map_service.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/maps")
@Tag(name = "Cartografia y Mapas", description = "Operaciones para gestionar mapas del mundo, mazmorras y puntos de interes geografico")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class MapController {

    @Autowired
    private MapService mapService;

    @Operation(summary = "Obtiene el catalogo completo de todos los mapas creados para las campanas")
    @GetMapping
    public ResponseEntity<EntityModel<List<Mapa>>> listarMapas() {
        List<Mapa> mapas = mapService.listarMapas();
        if (mapas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        EntityModel<List<Mapa>> model = EntityModel.of(mapas);
        model.add(linkTo(methodOn(MapController.class).listarMapas()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Busca la informacion detallada de un mapa especifico mediante su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mapa>> buscarMapa(@PathVariable Integer id) {
        Optional<Mapa> mapa = mapService.buscarMapa(id);
        return mapa.map(value -> {
                    EntityModel<Mapa> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(MapController.class).buscarMapa(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crea un nuevo mapa general (ej: Continente central o nivel 1 de una mazmorra)")
    @PostMapping
    public ResponseEntity<Mapa> crearMapa(@Valid @RequestBody Mapa mapa) {
        Mapa nuevoMapa = mapService.crearMapa(mapa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMapa);
    }

    @Operation(summary = "Lista todos los puntos de interes (ciudades, tabernas, trampas) vinculados a un mapa especifico")
    @GetMapping("/{id}/ubicaciones")
    public ResponseEntity<EntityModel<List<Ubicacion>>> listarUbicaciones(@PathVariable Integer id) {
        List<Ubicacion> ubicaciones = mapService.listarUbicacionesDeMapa(id);
        if (ubicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        EntityModel<List<Ubicacion>> model = EntityModel.of(ubicaciones);
        model.add(linkTo(methodOn(MapController.class).listarUbicaciones(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Registra una nueva ubicacion o punto de interes dentro de un mapa existente")
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
    public ResponseEntity<EntityModel<Ubicacion>> buscarUbicacion(@PathVariable Integer id) {
        Optional<Ubicacion> ubicacion = mapService.buscarUbicacion(id);
        return ubicacion.map(value -> {
                    EntityModel<Ubicacion> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(MapController.class).buscarUbicacion(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
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

package com.DragonForge.asset_service.controller;

import com.DragonForge.asset_service.model.ArchivoActivo;
import com.DragonForge.asset_service.model.CarpetaActivo;
import com.DragonForge.asset_service.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/assets")
@Tag(name = "Recursos Multimedia", description = "Operaciones para gestionar imágenes de mapas, avatares de jugadores y tokens de monstruos")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Operation(summary = "Obtiene el listado completo de todas las carpetas (ej: Mapas de Mazmorras, Tokens de Enemigos)")
    @GetMapping("/carpetas")
    public ResponseEntity<CollectionModel<CarpetaActivo>> listarCarpetas() {
        List<CarpetaActivo> carpetas = assetService.listarCarpetas();
        if (carpetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<CarpetaActivo> model = CollectionModel.of(carpetas);
        model.add(linkTo(methodOn(AssetController.class).listarCarpetas()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Crea una nueva carpeta para organizar los recursos multimedia")
    @PostMapping("/carpetas")
    public ResponseEntity<CarpetaActivo> crearCarpeta(@Valid @RequestBody CarpetaActivo carpeta) {
        CarpetaActivo nuevaCarpeta = assetService.crearCarpeta(carpeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCarpeta);
    }

    @Operation(summary = "Personaliza una carpeta existente")
    @PutMapping("/carpetas/{id}")
    public ResponseEntity<CarpetaActivo> actualizarCarpeta(@PathVariable Integer id, @Valid @RequestBody CarpetaActivo carpeta) {
        try {
            return ResponseEntity.ok(assetService.actualizarCarpeta(id, carpeta));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una carpeta y sus archivos")
    @DeleteMapping("/carpetas/{id}")
    public ResponseEntity<Void> eliminarCarpeta(@PathVariable Integer id) {
        try {
            assetService.eliminarCarpeta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Lista todos los archivos (imágenes, sonidos, etc.) contenidos dentro de una carpeta específica")
    @GetMapping("/carpetas/{id}/archivos")
    public ResponseEntity<CollectionModel<ArchivoActivo>> listarArchivos(@PathVariable Integer id) {
        List<ArchivoActivo> archivos = assetService.listarArchivosDeCarpeta(id);
        if (archivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<ArchivoActivo> model = CollectionModel.of(archivos);
        model.add(linkTo(methodOn(AssetController.class).listarArchivos(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Busca un archivo por ID")
    @GetMapping("/archivos/{id}")
    public ResponseEntity<EntityModel<ArchivoActivo>> buscarArchivo(@PathVariable Integer id) {
        Optional<ArchivoActivo> archivo = assetService.buscarArchivo(id);
        return archivo.map(value -> {
                    EntityModel<ArchivoActivo> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(AssetController.class).buscarArchivo(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Sube y registra un nuevo archivo multimedia asociándolo a una carpeta existente")
    @PostMapping("/carpetas/{id}/archivos")
    public ResponseEntity<?> subirArchivo(@PathVariable Integer id, @Valid @RequestBody ArchivoActivo archivo) {
        try {
            ArchivoActivo nuevoArchivo = assetService.guardarArchivo(id, archivo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArchivo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Personaliza un archivo existente")
    @PutMapping("/archivos/{id}")
    public ResponseEntity<?> actualizarArchivo(@PathVariable Integer id, @Valid @RequestBody ArchivoActivo archivo) {
        try {
            return ResponseEntity.ok(assetService.actualizarArchivo(id, archivo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un archivo por ID")
    @DeleteMapping("/archivos/{id}")
    public ResponseEntity<Void> eliminarArchivo(@PathVariable Integer id) {
        try {
            assetService.eliminarArchivo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
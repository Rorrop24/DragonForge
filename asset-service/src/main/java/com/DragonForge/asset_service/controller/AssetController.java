package com.DragonForge.asset_service.controller;

import com.DragonForge.asset_service.model.ArchivoActivo;
import com.DragonForge.asset_service.model.CarpetaActivo;
import com.DragonForge.asset_service.service.AssetService;
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
@RequestMapping("/api/v1/assets")
@Tag(name = "Recursos Multimedia", description = "Operaciones para gestionar imágenes de mapas, avatares de jugadores y tokens de monstruos")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Operation(summary = "Obtiene el listado completo de todas las carpetas (ej: Mapas de Mazmorras, Tokens de Enemigos)")
    @GetMapping("/carpetas")
    public ResponseEntity<List<CarpetaActivo>> listarCarpetas() {
        List<CarpetaActivo> carpetas = assetService.listarCarpetas();
        if (carpetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carpetas);
    }

    @Operation(summary = "Crea una nueva carpeta para organizar los recursos multimedia")
    @PostMapping("/carpetas")
    public ResponseEntity<CarpetaActivo> crearCarpeta(@Valid @RequestBody CarpetaActivo carpeta) {
        CarpetaActivo nuevaCarpeta = assetService.crearCarpeta(carpeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCarpeta);
    }

    @Operation(summary = "Lista todos los archivos (imágenes, sonidos, etc.) contenidos dentro de una carpeta específica")
    @GetMapping("/carpetas/{id}/archivos")
    public ResponseEntity<List<ArchivoActivo>> listarArchivos(@PathVariable Integer id) {
        List<ArchivoActivo> archivos = assetService.listarArchivosDeCarpeta(id);
        if (archivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(archivos);
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
}
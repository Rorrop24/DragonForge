package com.DragonForge.asset_service.controller;

import com.DragonForge.asset_service.model.ArchivoActivo;
import com.DragonForge.asset_service.model.CarpetaActivo;
import com.DragonForge.asset_service.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/carpetas")
    public ResponseEntity<List<CarpetaActivo>> listarCarpetas() {
        List<CarpetaActivo> carpetas = assetService.listarCarpetas();
        if (carpetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carpetas);
    }

    @PostMapping("/carpetas")
    public ResponseEntity<CarpetaActivo> crearCarpeta(@Valid @RequestBody CarpetaActivo carpeta) {
        CarpetaActivo nuevaCarpeta = assetService.crearCarpeta(carpeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCarpeta);
    }

    @GetMapping("/carpetas/{id}/archivos")
    public ResponseEntity<List<ArchivoActivo>> listarArchivos(@PathVariable Integer id) {
        List<ArchivoActivo> archivos = assetService.listarArchivosDeCarpeta(id);
        if (archivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(archivos);
    }

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
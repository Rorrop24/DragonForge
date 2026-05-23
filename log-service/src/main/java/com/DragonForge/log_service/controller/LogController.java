package com.DragonForge.log_service.controller;

import com.DragonForge.log_service.model.Diario;
import com.DragonForge.log_service.model.Entrada;
import com.DragonForge.log_service.service.LogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/diarios")
    public ResponseEntity<List<Diario>> listarDiarios() {
        List<Diario> diarios = logService.listarDiarios();
        if (diarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(diarios);
    }

    @GetMapping("/diarios/{id}")
    public ResponseEntity<Diario> buscarDiario(@PathVariable Integer id) {
        Optional<Diario> diario = logService.buscarDiario(id);
        return diario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/diarios")
    public ResponseEntity<Diario> crearDiario(@Valid @RequestBody Diario diario) {
        Diario nuevoDiario = logService.crearDiario(diario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiario);
    }

    @GetMapping("/diarios/{id}/entradas")
    public ResponseEntity<List<Entrada>> verEntradas(@PathVariable Integer id) {
        List<Entrada> entradas = logService.verEntradasDeDiario(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @PostMapping("/diarios/{id}/entradas")
    public ResponseEntity<?> agregarEntrada(@PathVariable Integer id, @Valid @RequestBody Entrada entrada) {
        try {
            Entrada nuevaEntrada = logService.agregarEntrada(id, entrada);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEntrada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
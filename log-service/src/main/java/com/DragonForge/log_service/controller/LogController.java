package com.DragonForge.log_service.controller;

import com.DragonForge.log_service.model.Diario;
import com.DragonForge.log_service.model.Entrada;
import com.DragonForge.log_service.service.LogService;
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
@RequestMapping("/api/v1/logs")
@Tag(name = "Diario de Aventuras", description = "Operaciones para gestionar los diarios de campaña y el registro de eventos, decisiones o combates")
public class LogController {

    @Autowired
    private LogService logService;

    @Operation(summary = "Obtiene el listado completo de todos los diarios de campaña activos")
    @GetMapping("/diarios")
    public ResponseEntity<List<Diario>> listarDiarios() {
        List<Diario> diarios = logService.listarDiarios();
        if (diarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(diarios);
    }

    @Operation(summary = "Busca los detalles de un diario de aventuras específico mediante su ID")
    @GetMapping("/diarios/{id}")
    public ResponseEntity<Diario> buscarDiario(@PathVariable Integer id) {
        Optional<Diario> diario = logService.buscarDiario(id);
        return diario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Inicia un nuevo diario de aventuras para una campaña o grupo de jugadores")
    @PostMapping("/diarios")
    public ResponseEntity<Diario> crearDiario(@Valid @RequestBody Diario diario) {
        Diario nuevoDiario = logService.crearDiario(diario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiario);
    }

    @Operation(summary = "Lista cronológicamente todas las entradas (eventos, notas, historia) escritas en un diario")
    @GetMapping("/diarios/{id}/entradas")
    public ResponseEntity<List<Entrada>> verEntradas(@PathVariable Integer id) {
        List<Entrada> entradas = logService.verEntradasDeDiario(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(entradas);
    }

    @Operation(summary = "Registra una nueva entrada en el diario (ej: daño recibido, botín encontrado o progreso de la historia)")
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
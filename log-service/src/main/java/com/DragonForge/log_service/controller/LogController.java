package com.DragonForge.log_service.controller;

import com.DragonForge.log_service.model.Diario;
import com.DragonForge.log_service.model.Entrada;
import com.DragonForge.log_service.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/logs")
@Tag(name = "Diario de Aventuras", description = "Operaciones para gestionar los diarios de campana y el registro de eventos, decisiones o combates")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class LogController {

    @Autowired
    private LogService logService;

    @Operation(summary = "Obtiene el listado completo de todos los diarios de campana activos")
    @GetMapping("/diarios")
    public ResponseEntity<CollectionModel<Diario>> listarDiarios() {
        List<Diario> diarios = logService.listarDiarios();
        if (diarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Diario> model = CollectionModel.of(diarios);
        model.add(linkTo(methodOn(LogController.class).listarDiarios()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Busca los detalles de un diario de aventuras especifico mediante su ID")
    @GetMapping("/diarios/{id}")
    public ResponseEntity<EntityModel<Diario>> buscarDiario(@PathVariable Integer id) {
        Optional<Diario> diario = logService.buscarDiario(id);
        return diario.map(value -> {
                    EntityModel<Diario> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(LogController.class).buscarDiario(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Inicia un nuevo diario de aventuras para una campana o grupo de jugadores")
    @PostMapping("/diarios")
    public ResponseEntity<Diario> crearDiario(@Valid @RequestBody Diario diario) {
        Diario nuevoDiario = logService.crearDiario(diario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiario);
    }

    @Operation(summary = "Lista cronologicamente todas las entradas (eventos, notas, historia) escritas en un diario")
    @GetMapping("/diarios/{id}/entradas")
    public ResponseEntity<CollectionModel<Entrada>> verEntradas(@PathVariable Integer id) {
        List<Entrada> entradas = logService.verEntradasDeDiario(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Entrada> model = CollectionModel.of(entradas);
        model.add(linkTo(methodOn(LogController.class).verEntradas(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Registra una nueva entrada en el diario (ej: dano recibido, botin encontrado o progreso de la historia)")
    @PostMapping("/diarios/{id}/entradas")
    public ResponseEntity<?> agregarEntrada(@PathVariable Integer id, @Valid @RequestBody Entrada entrada) {
        try {
            Entrada nuevaEntrada = logService.agregarEntrada(id, entrada);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEntrada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Personaliza un diario existente")
    @PutMapping("/diarios/{id}")
    public ResponseEntity<Diario> actualizarDiario(@PathVariable Integer id, @Valid @RequestBody Diario diario) {
        try {
            return ResponseEntity.ok(logService.actualizarDiario(id, diario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un diario y sus entradas")
    @DeleteMapping("/diarios/{id}")
    public ResponseEntity<Void> eliminarDiario(@PathVariable Integer id) {
        try {
            logService.eliminarDiario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Busca una entrada de diario mediante su ID")
    @GetMapping("/entradas/{id}")
    public ResponseEntity<EntityModel<Entrada>> buscarEntrada(@PathVariable Integer id) {
        Optional<Entrada> entrada = logService.buscarEntrada(id);
        return entrada.map(value -> {
                    EntityModel<Entrada> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(LogController.class).buscarEntrada(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Personaliza una entrada de diario")
    @PutMapping("/entradas/{id}")
    public ResponseEntity<?> actualizarEntrada(@PathVariable Integer id, @Valid @RequestBody Entrada entrada) {
        try {
            return ResponseEntity.ok(logService.actualizarEntrada(id, entrada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una entrada de diario")
    @DeleteMapping("/entradas/{id}")
    public ResponseEntity<Void> eliminarEntrada(@PathVariable Integer id) {
        try {
            logService.eliminarEntrada(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.DragonForge.character_service.controller;

import com.DragonForge.character_service.dto.PersonajeDTO;
import com.DragonForge.character_service.model.Personaje;
import com.DragonForge.character_service.service.PersonajeService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/personajes")
@Tag(name = "Personajes", description = "Operaciones para gestionar las hojas de personaje de Dungeons & Dragons (estadisticas, clases, razas)")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class PersonajeController {

    @Autowired
    private PersonajeService service;

    @Operation(summary = "Obtiene el listado completo de todos los personajes creados en la plataforma")
    @GetMapping
    public ResponseEntity<CollectionModel<PersonajeDTO>> listar() {
        List<PersonajeDTO> list = service.listarTodos();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<PersonajeDTO> model = CollectionModel.of(list);
        model.add(linkTo(methodOn(PersonajeController.class).listar()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Busca y recupera la hoja de personaje detallada mediante su ID unico")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PersonajeDTO>> buscar(@PathVariable Integer id) {
        try {
            PersonajeDTO dto = service.buscarPorId(id);
            EntityModel<PersonajeDTO> model = EntityModel.of(dto);
            model.add(linkTo(methodOn(PersonajeController.class).buscar(id)).withSelfRel());
            return ResponseEntity.ok(model);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Forja un nuevo personaje asignandole su raza, clase y estadisticas base")
    @PostMapping
    public ResponseEntity<PersonajeDTO> registrar(@Valid @RequestBody Personaje personaje) {
        PersonajeDTO nuevo = service.crearPersonaje(personaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Personaliza o actualiza los datos de un personaje existente")
    @PutMapping("/{id}")
    public ResponseEntity<PersonajeDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody Personaje personaje) {
        try {
            PersonajeDTO actualizado = service.actualizarPersonaje(id, personaje);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un personaje mediante su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            service.eliminarPersonaje(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.DragonForge.compendium_service.controller;

import com.DragonForge.compendium_service.model.CategoriaCompendio;
import com.DragonForge.compendium_service.model.EntradaCompendio;
import com.DragonForge.compendium_service.service.CompendiumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/v1/compendium")
@Tag(name = "Compendio y Bestiario", description = "Operaciones para gestionar el conocimiento del mundo: categorias, monstruos, hechizos y reglas de D&D")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class CompendiumController {

    @Autowired
    private CompendiumService compendiumService;

    @Operation(
            summary = "Listar categorias del compendio",
            description = "Retorna las categorias maestras usadas para organizar monstruos, hechizos, reglas y objetos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorias obtenidas correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoriaCompendio.class)))),
            @ApiResponse(responseCode = "204", description = "No existen categorias registradas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/categorias")
    public ResponseEntity<CollectionModel<CategoriaCompendio>> listarCategorias() {
        List<CategoriaCompendio> categorias = compendiumService.listarCategorias();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<CategoriaCompendio> model = CollectionModel.of(categorias);
        model.add(linkTo(methodOn(CompendiumController.class).listarCategorias()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Crea una nueva categoria maestra para organizar el conocimiento")
    @PostMapping("/categorias")
    public ResponseEntity<CategoriaCompendio> crearCategoria(@Valid @RequestBody CategoriaCompendio categoria) {
        CategoriaCompendio nuevaCategoria = compendiumService.crearCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @Operation(
            summary = "Listar entradas del compendio",
            description = "Retorna todas las entradas del compendio, incluyendo lore, bestiario, hechizos y reglas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entradas obtenidas correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EntradaCompendio.class)))),
            @ApiResponse(responseCode = "204", description = "No existen entradas registradas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/entradas")
    public ResponseEntity<CollectionModel<EntradaCompendio>> listarTodasLasEntradas() {
        List<EntradaCompendio> entradas = compendiumService.listarTodasLasEntradas();
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<EntradaCompendio> model = CollectionModel.of(entradas);
        model.add(linkTo(methodOn(CompendiumController.class).listarTodasLasEntradas()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(
            summary = "Buscar entrada por ID",
            description = "Obtiene el detalle completo de una entrada del compendio segun su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entrada encontrada", content = @Content(schema = @Schema(implementation = EntradaCompendio.class))),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/entradas/{id}")
    public ResponseEntity<EntityModel<EntradaCompendio>> buscarEntrada(@Parameter(description = "ID de la entrada del compendio", example = "1", required = true) @PathVariable Integer id) {
        Optional<EntradaCompendio> entrada = compendiumService.buscarEntradaPorId(id);
        return entrada.map(value -> {
                    EntityModel<EntradaCompendio> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(CompendiumController.class).buscarEntrada(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listar entradas por categoria",
            description = "Retorna las entradas asociadas a una categoria concreta del compendio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entradas de la categoria obtenidas correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EntradaCompendio.class)))),
            @ApiResponse(responseCode = "204", description = "La categoria no tiene entradas registradas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/categorias/{id}/entradas")
    public ResponseEntity<CollectionModel<EntradaCompendio>> buscarPorCategoria(@Parameter(description = "ID de la categoria del compendio", example = "1", required = true) @PathVariable Integer id) {
        List<EntradaCompendio> entradas = compendiumService.buscarEntradasPorCategoria(id);
        if (entradas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<EntradaCompendio> model = CollectionModel.of(entradas);
        model.add(linkTo(methodOn(CompendiumController.class).buscarPorCategoria(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Registra una nueva entrada (monstruo, regla, hechizo) asociandola directamente a una categoria")
    @PostMapping("/categorias/{id}/entradas")
    public ResponseEntity<?> agregarEntrada(@PathVariable Integer id, @Valid @RequestBody EntradaCompendio entrada) {
        try {
            EntradaCompendio nuevaEntrada = compendiumService.agregarEntrada(id, entrada);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEntrada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Personaliza una categoria del compendio")
    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaCompendio> actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaCompendio categoria) {
        try {
            return ResponseEntity.ok(compendiumService.actualizarCategoria(id, categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una categoria del compendio")
    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        try {
            compendiumService.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Personaliza una entrada del compendio")
    @PutMapping("/entradas/{id}")
    public ResponseEntity<?> actualizarEntrada(@PathVariable Integer id, @Valid @RequestBody EntradaCompendio entrada) {
        try {
            return ResponseEntity.ok(compendiumService.actualizarEntrada(id, entrada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una entrada del compendio")
    @DeleteMapping("/entradas/{id}")
    public ResponseEntity<Void> eliminarEntrada(@PathVariable Integer id) {
        try {
            compendiumService.eliminarEntrada(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
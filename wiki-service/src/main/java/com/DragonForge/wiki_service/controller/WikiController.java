package com.DragonForge.wiki_service.controller;

import com.DragonForge.wiki_service.model.Articulo;
import com.DragonForge.wiki_service.model.Comentario;
import com.DragonForge.wiki_service.service.WikiService;
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
@RequestMapping("/api/v1/wiki")
@Tag(name = "Enciclopedia y Lore (Wiki)", description = "Operaciones para gestionar los articulos de la historia del mundo, facciones, ciudades y los comentarios de los jugadores")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class WikiController {

    @Autowired
    private WikiService wikiService;

    @Operation(summary = "Listar articulos de wiki", description = "Retorna todos los articulos y documentos publicados en la wiki del mundo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Articulos obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Articulo.class)))),
            @ApiResponse(responseCode = "204", description = "No existen articulos registrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/articulos")
    public ResponseEntity<CollectionModel<Articulo>> listarTodos() {
        List<Articulo> articulos = wikiService.listarArticulos();
        if (articulos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Articulo> model = CollectionModel.of(articulos);
        model.add(linkTo(methodOn(WikiController.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Buscar articulo por ID", description = "Obtiene el contenido detallado de un articulo especifico de la wiki.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Articulo encontrado", content = @Content(schema = @Schema(implementation = Articulo.class))),
            @ApiResponse(responseCode = "404", description = "Articulo no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/articulos/{id}")
    public ResponseEntity<EntityModel<Articulo>> buscarPorId(@Parameter(description = "ID del articulo", example = "1", required = true) @PathVariable Integer id) {
        Optional<Articulo> articulo = wikiService.buscarArticulo(id);
        return articulo.map(value -> {
                    EntityModel<Articulo> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(WikiController.class).buscarPorId(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Escribe y publica un nuevo articulo de lore (ej: la historia del reino) asociandolo al ID del Dungeon Master o autor")
    @PostMapping("/articulos/{autorId}")
    public ResponseEntity<Articulo> crearArticulo(@Valid @RequestBody Articulo articulo, @PathVariable Integer autorId) {
        Articulo nuevoArticulo = wikiService.crearArticulo(articulo, autorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArticulo);
    }

    @Operation(summary = "Listar comentarios de un articulo", description = "Retorna los comentarios y notas de discusion asociados a un articulo de wiki.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentarios obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comentario.class)))),
            @ApiResponse(responseCode = "204", description = "El articulo no tiene comentarios registrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/articulos/{id}/comentarios")
    public ResponseEntity<CollectionModel<Comentario>> verComentarios(@Parameter(description = "ID del articulo", example = "1", required = true) @PathVariable Integer id) {
        List<Comentario> comentarios = wikiService.verComentariosDeArticulo(id);
        if (comentarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Comentario> model = CollectionModel.of(comentarios);
        model.add(linkTo(methodOn(WikiController.class).verComentarios(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Anade un nuevo comentario o nota de discusion al final de un articulo existente de la wiki")
    @PostMapping("/articulos/{id}/comentarios")
    public ResponseEntity<?> agregarComentario(@PathVariable Integer id, @Valid @RequestBody Comentario comentario) {
        try {
            Comentario nuevoComentario = wikiService.agregarComentario(id, comentario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
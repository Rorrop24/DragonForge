package com.DragonForge.wiki_service.controller;

import com.DragonForge.wiki_service.model.Articulo;
import com.DragonForge.wiki_service.model.Comentario;
import com.DragonForge.wiki_service.service.WikiService;
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
@RequestMapping("/api/v1/wiki")
@Tag(name = "Enciclopedia y Lore (Wiki)", description = "Operaciones para gestionar los artículos de la historia del mundo, facciones, ciudades y los comentarios de los jugadores")
public class WikiController {

    @Autowired
    private WikiService wikiService;

    @Operation(summary = "Obtiene el catálogo completo de todos los artículos y documentos publicados en la wiki")
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

    @Operation(summary = "Busca y recupera el pergamino o contenido detallado de un artículo específico mediante su ID")
    @GetMapping("/articulos/{id}")
    public ResponseEntity<EntityModel<Articulo>> buscarPorId(@PathVariable Integer id) {
        Optional<Articulo> articulo = wikiService.buscarArticulo(id);
        return articulo.map(value -> {
                    EntityModel<Articulo> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(WikiController.class).buscarPorId(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Escribe y publica un nuevo artículo de lore (ej: la historia del reino) asociándolo al ID del Dungeon Master o autor")
    @PostMapping("/articulos/{autorId}")
    public ResponseEntity<Articulo> crearArticulo(@Valid @RequestBody Articulo articulo, @PathVariable Integer autorId) {
        Articulo nuevoArticulo = wikiService.crearArticulo(articulo, autorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArticulo);
    }

    @Operation(summary = "Lista todos los comentarios, teorías o notas dejadas por los jugadores en un artículo específico")
    @GetMapping("/articulos/{id}/comentarios")
    public ResponseEntity<CollectionModel<Comentario>> verComentarios(@PathVariable Integer id) {
        List<Comentario> comentarios = wikiService.verComentariosDeArticulo(id);
        if (comentarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Comentario> model = CollectionModel.of(comentarios);
        model.add(linkTo(methodOn(WikiController.class).verComentarios(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Añade un nuevo comentario o nota de discusión al final de un artículo existente de la wiki")
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
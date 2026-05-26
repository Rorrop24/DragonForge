package com.DragonForge.wiki_service.controller;

import com.DragonForge.wiki_service.model.Articulo;
import com.DragonForge.wiki_service.model.Comentario;
import com.DragonForge.wiki_service.service.WikiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wiki")
public class WikiController {

    @Autowired
    private WikiService wikiService;

    @GetMapping("/articulos")
    public ResponseEntity<List<Articulo>> listarTodos() {
        List<Articulo> articulos = wikiService.listarArticulos();
        if (articulos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articulos);
    }

    @GetMapping("/articulos/{id}")
    public ResponseEntity<Articulo> buscarPorId(@PathVariable Integer id) {
        Optional<Articulo> articulo = wikiService.buscarArticulo(id);
        return articulo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/articulos/{autorId}")
    public ResponseEntity<Articulo> crearArticulo(@Valid @RequestBody Articulo articulo, @PathVariable Integer autorId) {
        Articulo nuevoArticulo = wikiService.crearArticulo(articulo, autorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArticulo);
    }

    @GetMapping("/articulos/{id}/comentarios")
    public ResponseEntity<List<Comentario>> verComentarios(@PathVariable Integer id) {
        List<Comentario> comentarios = wikiService.verComentariosDeArticulo(id);
        if (comentarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comentarios);
    }

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
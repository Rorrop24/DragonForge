package com.DragonForge.user_service.controller;

import com.DragonForge.user_service.model.Campana;
import com.DragonForge.user_service.model.Usuario;
import com.DragonForge.user_service.service.UserService;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios y Campanas", description = "Operaciones para gestionar las cuentas de Jugadores, Dungeon Masters y sus campanas de juego activas")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Obtiene el listado completo de todos los usuarios (Jugadores y DMs) registrados en la plataforma")
    @GetMapping
    public ResponseEntity<CollectionModel<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = userService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Usuario> model = CollectionModel.of(usuarios);
        model.add(linkTo(methodOn(UserController.class).listarUsuarios()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Busca los datos de perfil de un usuario especifico mediante su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> buscarUsuario(@PathVariable Integer id) {
        Optional<Usuario> usuario = userService.buscarUsuario(id);
        return usuario.map(value -> {
                    EntityModel<Usuario> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(UserController.class).buscarUsuario(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registra una nueva cuenta de usuario en el sistema")
    @PostMapping
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = userService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(summary = "Lista todas las campanas de Dungeons & Dragons en las que participa o que dirige un usuario especifico")
    @GetMapping("/{id}/campanas")
    public ResponseEntity<CollectionModel<Campana>> listarCampanas(@PathVariable Integer id) {
        List<Campana> campanas = userService.verCampanasDeUsuario(id);
        if (campanas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Campana> model = CollectionModel.of(campanas);
        model.add(linkTo(methodOn(UserController.class).listarCampanas(id)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Crea y vincula una nueva campana de rol al perfil de un usuario")
    @PostMapping("/{id}/campanas")
    public ResponseEntity<?> agregarCampana(@PathVariable Integer id, @Valid @RequestBody Campana campana) {
        try {
            Campana nuevaCampana = userService.crearCampana(id, campana);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCampana);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Personaliza los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(userService.actualizarUsuario(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un usuario mediante su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        try {
            userService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Busca una campana mediante su ID")
    @GetMapping("/campanas/{id}")
    public ResponseEntity<EntityModel<Campana>> buscarCampana(@PathVariable Integer id) {
        Optional<Campana> campana = userService.buscarCampana(id);
        return campana.map(value -> {
                    EntityModel<Campana> model = EntityModel.of(value);
                    model.add(linkTo(methodOn(UserController.class).buscarCampana(id)).withSelfRel());
                    return ResponseEntity.ok(model);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Personaliza una campana existente")
    @PutMapping("/campanas/{id}")
    public ResponseEntity<?> actualizarCampana(@PathVariable Integer id, @Valid @RequestBody Campana campana) {
        try {
            return ResponseEntity.ok(userService.actualizarCampana(id, campana));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una campana mediante su ID")
    @DeleteMapping("/campanas/{id}")
    public ResponseEntity<Void> eliminarCampana(@PathVariable Integer id) {
        try {
            userService.eliminarCampana(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
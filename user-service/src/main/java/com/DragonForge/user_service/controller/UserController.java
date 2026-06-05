package com.DragonForge.user_service.controller;

import com.DragonForge.user_service.model.Campana;
import com.DragonForge.user_service.model.Usuario;
import com.DragonForge.user_service.service.UserService;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios y Campañas", description = "Operaciones para gestionar las cuentas de Jugadores, Dungeon Masters y sus campañas de juego activas")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Obtiene el listado completo de todos los usuarios (Jugadores y DMs) registrados en la plataforma")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = userService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Busca los datos de perfil de un usuario específico mediante su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id) {
        Optional<Usuario> usuario = userService.buscarUsuario(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registra una nueva cuenta de usuario en el sistema")
    @PostMapping
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = userService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(summary = "Lista todas las campañas de Dungeons & Dragons en las que participa o que dirige un usuario específico")
    @GetMapping("/{id}/campanas")
    public ResponseEntity<List<Campana>> listarCampanas(@PathVariable Integer id) {
        List<Campana> campanas = userService.verCampanasDeUsuario(id);
        if (campanas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(campanas);
    }

    @Operation(summary = "Crea y vincula una nueva campaña de rol al perfil de un usuario")
    @PostMapping("/{id}/campanas")
    public ResponseEntity<?> agregarCampana(@PathVariable Integer id, @Valid @RequestBody Campana campana) {
        try {
            Campana nuevaCampana = userService.crearCampana(id, campana);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCampana);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
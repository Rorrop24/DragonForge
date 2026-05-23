package com.DragonForge.user_service.controller;

import com.DragonForge.user_service.model.Campana;
import com.DragonForge.user_service.model.Usuario;
import com.DragonForge.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = userService.listarUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id) {
        Optional<Usuario> usuario = userService.buscarUsuario(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = userService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping("/{id}/campanas")
    public ResponseEntity<List<Campana>> listarCampanas(@PathVariable Integer id) {
        List<Campana> campanas = userService.verCampanasDeUsuario(id);
        if (campanas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(campanas);
    }

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
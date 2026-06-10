package com.DragonForge.notification_service.controller;

import com.DragonForge.notification_service.model.Buzon;
import com.DragonForge.notification_service.model.Notificacion;
import com.DragonForge.notification_service.service.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@Tag(name = "Mensajería y Notificaciones", description = "Operaciones para gestionar buzones y enviar alertas de juego, turnos o mensajes del DM a los jugadores")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Obtiene una lista de todos los buzones de mensajería creados en el sistema")
    @GetMapping("/buzones")
    public ResponseEntity<List<Buzon>> listarBuzones() {
        List<Buzon> buzones = notificationService.listarBuzones();
        return buzones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(buzones);
    }

    @Operation(summary = "Crea un nuevo buzón de notificaciones (idealmente al registrar un nuevo usuario en la plataforma)")
    @PostMapping("/buzones")
    public ResponseEntity<Buzon> crearBuzon(@Valid @RequestBody Buzon buzon) {
        Buzon nuevoBuzon = notificationService.crearBuzon(buzon);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoBuzon);
    }

    @Operation(summary = "Busca y recupera el buzón de notificaciones asociado a la ID de un usuario específico")
    @GetMapping("/buzones/usuario/{usuarioId}")
    public ResponseEntity<Buzon> buscarPorUsuarioId(@PathVariable Integer usuarioId) {
        Optional<Buzon> buzon = notificationService.buscarBuzonPorUsuarioId(usuarioId);
        return buzon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista todos los mensajes y alertas de juego guardados en un buzón específico")
    @GetMapping("/buzones/{buzonId}/mensajes")
    public ResponseEntity<List<Notificacion>> listarNotificaciones(@PathVariable Integer buzonId) {
        List<Notificacion> notificaciones = notificationService.listarNotificaciones(buzonId);
        return notificaciones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notificaciones);
    }

    @Operation(summary = "Filtra y lista únicamente los mensajes que aún no han sido leídos por el jugador")
    @GetMapping("/buzones/{buzonId}/mensajes/noleidos")
    public ResponseEntity<List<Notificacion>> listarNoLeidas(@PathVariable Integer buzonId) {
        List<Notificacion> noLeidas = notificationService.listarNotificacionesNoLeidas(buzonId);
        return noLeidas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(noLeidas);
    }

    @Operation(summary = "Envía una nueva notificación a un buzón (ej: 'Es tu turno', 'Pifia crítica' o 'Recibiste 10 puntos de daño')")
    @PostMapping("/buzones/{buzonId}/mensajes")
    public ResponseEntity<?> enviarNotificacion(@PathVariable Integer buzonId, @Valid @RequestBody Notificacion notificacion) {
        try {
            Notificacion nuevaNotificacion = notificationService.enviarNotificacion(buzonId, notificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Marca una notificación específica como 'leída' para que deje de aparecer como pendiente")
    @PatchMapping("/mensajes/{id}/leer")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificacion actualizada = notificationService.marcarComoLeida(id);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Busca un buzon mediante su ID")
    @GetMapping("/buzones/{id}")
    public ResponseEntity<Buzon> buscarBuzon(@PathVariable Integer id) {
        Optional<Buzon> buzon = notificationService.buscarBuzon(id);
        return buzon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Personaliza un buzon existente")
    @PutMapping("/buzones/{id}")
    public ResponseEntity<Buzon> actualizarBuzon(@PathVariable Integer id, @Valid @RequestBody Buzon buzon) {
        try {
            return ResponseEntity.ok(notificationService.actualizarBuzon(id, buzon));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina un buzon y sus mensajes")
    @DeleteMapping("/buzones/{id}")
    public ResponseEntity<Void> eliminarBuzon(@PathVariable Integer id) {
        try {
            notificationService.eliminarBuzon(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Busca una notificacion mediante su ID")
    @GetMapping("/mensajes/{id}")
    public ResponseEntity<Notificacion> buscarNotificacion(@PathVariable Integer id) {
        Optional<Notificacion> notificacion = notificationService.buscarNotificacion(id);
        return notificacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Personaliza una notificacion existente")
    @PutMapping("/mensajes/{id}")
    public ResponseEntity<?> actualizarNotificacion(@PathVariable Integer id, @Valid @RequestBody Notificacion notificacion) {
        try {
            return ResponseEntity.ok(notificationService.actualizarNotificacion(id, notificacion));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Elimina una notificacion mediante su ID")
    @DeleteMapping("/mensajes/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Integer id) {
        try {
            notificationService.eliminarNotificacion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

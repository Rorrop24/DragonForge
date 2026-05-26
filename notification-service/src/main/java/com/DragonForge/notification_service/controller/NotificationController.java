package com.DragonForge.notification_service.controller;

import com.DragonForge.notification_service.model.Buzon;
import com.DragonForge.notification_service.model.Notificacion;
import com.DragonForge.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/buzones")
    public ResponseEntity<List<Buzon>> listarBuzones() {
        List<Buzon> buzones = notificationService.listarBuzones();
        return buzones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(buzones);
    }

    @PostMapping("/buzones")
    public ResponseEntity<Buzon> crearBuzon(@Valid @RequestBody Buzon buzon) {
        Buzon nuevoBuzon = notificationService.crearBuzon(buzon);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoBuzon);
    }

    @GetMapping("/buzones/usuario/{usuarioId}")
    public ResponseEntity<Buzon> buscarPorUsuarioId(@PathVariable Integer usuarioId) {
        Optional<Buzon> buzon = notificationService.buscarBuzonPorUsuarioId(usuarioId);
        return buzon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/buzones/{buzonId}/mensajes")
    public ResponseEntity<List<Notificacion>> listarNotificaciones(@PathVariable Integer buzonId) {
        List<Notificacion> notificaciones = notificationService.listarNotificaciones(buzonId);
        return notificaciones.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/buzones/{buzonId}/mensajes/noleidos")
    public ResponseEntity<List<Notificacion>> listarNoLeidas(@PathVariable Integer buzonId) {
        List<Notificacion> noLeidas = notificationService.listarNotificacionesNoLeidas(buzonId);
        return noLeidas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(noLeidas);
    }

    @PostMapping("/buzones/{buzonId}/mensajes")
    public ResponseEntity<?> enviarNotificacion(@PathVariable Integer buzonId, @Valid @RequestBody Notificacion notificacion) {
        try {
            Notificacion nuevaNotificacion = notificationService.enviarNotificacion(buzonId, notificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/mensajes/{id}/leer")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificacion actualizada = notificationService.marcarComoLeida(id);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
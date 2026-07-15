package com.DragonForge.notification_service.controller;

import com.DragonForge.notification_service.model.Buzon;
import com.DragonForge.notification_service.model.Notificacion;
import com.DragonForge.notification_service.service.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@Tag(name = "Mensajeria y Notificaciones", description = "Operaciones para gestionar buzones y enviar alertas de juego, turnos o mensajes del DM a los jugadores")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Listar buzones", description = "Retorna todos los buzones de mensajeria y notificaciones creados en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buzones obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Buzon.class)))),
            @ApiResponse(responseCode = "204", description = "No existen buzones registrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/buzones")
    public ResponseEntity<CollectionModel<Buzon>> listarBuzones() {
        List<Buzon> buzones = notificationService.listarBuzones();
        if (buzones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Buzon> model = CollectionModel.of(buzones);
        model.add(linkTo(methodOn(NotificationController.class).listarBuzones()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Crea un nuevo buzon de notificaciones (idealmente al registrar un nuevo usuario en la plataforma)")
    @PostMapping("/buzones")
    public ResponseEntity<Buzon> crearBuzon(@Valid @RequestBody Buzon buzon) {
        Buzon nuevoBuzon = notificationService.crearBuzon(buzon);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoBuzon);
    }

    @Operation(summary = "Buscar buzon por usuario", description = "Obtiene el buzon de notificaciones asociado a un usuario especifico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buzon encontrado", content = @Content(schema = @Schema(implementation = Buzon.class))),
            @ApiResponse(responseCode = "404", description = "Buzon no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/buzones/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<Buzon>> buscarPorUsuarioId(@Parameter(description = "ID del usuario propietario del buzon", example = "1", required = true) @PathVariable Integer usuarioId) {
        Optional<Buzon> buzon = notificationService.buscarBuzonPorUsuarioId(usuarioId);
        return buzon.map(value -> {
            EntityModel<Buzon> model = EntityModel.of(value);
            model.add(linkTo(methodOn(NotificationController.class).buscarPorUsuarioId(usuarioId)).withSelfRel());
            return ResponseEntity.ok(model);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar mensajes de un buzon", description = "Retorna las notificaciones y alertas registradas en un buzon especifico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensajes obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notificacion.class)))),
            @ApiResponse(responseCode = "204", description = "El buzon no tiene mensajes registrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/buzones/{buzonId}/mensajes")
    public ResponseEntity<CollectionModel<Notificacion>> listarNotificaciones(@Parameter(description = "ID del buzon", example = "1", required = true) @PathVariable Integer buzonId) {
        List<Notificacion> notificaciones = notificationService.listarNotificaciones(buzonId);
        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Notificacion> model = CollectionModel.of(notificaciones);
        model.add(linkTo(methodOn(NotificationController.class).listarNotificaciones(buzonId)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Listar mensajes no leidos", description = "Retorna solo las notificaciones pendientes de lectura de un buzon.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensajes no leidos obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Notificacion.class)))),
            @ApiResponse(responseCode = "204", description = "El buzon no tiene mensajes no leidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/buzones/{buzonId}/mensajes/noleidos")
    public ResponseEntity<CollectionModel<Notificacion>> listarNoLeidas(@Parameter(description = "ID del buzon", example = "1", required = true) @PathVariable Integer buzonId) {
        List<Notificacion> noLeidas = notificationService.listarNotificacionesNoLeidas(buzonId);
        if (noLeidas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Notificacion> model = CollectionModel.of(noLeidas);
        model.add(linkTo(methodOn(NotificationController.class).listarNoLeidas(buzonId)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Envia una nueva notificacion a un buzon (ej: 'Es tu turno', 'Pifia critica' o 'Recibiste 10 puntos de dano')")
    @PostMapping("/buzones/{buzonId}/mensajes")
    public ResponseEntity<?> enviarNotificacion(@PathVariable Integer buzonId, @Valid @RequestBody Notificacion notificacion) {
        try {
            Notificacion nuevaNotificacion = notificationService.enviarNotificacion(buzonId, notificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Marca una notificacion especifica como 'leida' para que deje de aparecer como pendiente")
    @PatchMapping("/mensajes/{id}/leer")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificacion actualizada = notificationService.marcarComoLeida(id);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar buzon por ID", description = "Obtiene el detalle de un buzon de notificaciones especifico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buzon encontrado", content = @Content(schema = @Schema(implementation = Buzon.class))),
            @ApiResponse(responseCode = "404", description = "Buzon no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/buzones/{id}")
    public ResponseEntity<EntityModel<Buzon>> buscarBuzon(@Parameter(description = "ID del buzon", example = "1", required = true) @PathVariable Integer id) {
        Optional<Buzon> buzon = notificationService.buscarBuzon(id);
        return buzon.map(value -> {
            EntityModel<Buzon> model = EntityModel.of(value);
            model.add(linkTo(methodOn(NotificationController.class).buscarBuzon(id)).withSelfRel());
            return ResponseEntity.ok(model);
        }).orElseGet(() -> ResponseEntity.notFound().build());
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

    @Operation(summary = "Buscar notificacion por ID", description = "Obtiene el detalle de una notificacion o mensaje especifico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificacion encontrada", content = @Content(schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/mensajes/{id}")
    public ResponseEntity<EntityModel<Notificacion>> buscarNotificacion(@Parameter(description = "ID de la notificacion", example = "1", required = true) @PathVariable Integer id) {
        Optional<Notificacion> notificacion = notificationService.buscarNotificacion(id);
        return notificacion.map(value -> {
            EntityModel<Notificacion> model = EntityModel.of(value);
            model.add(linkTo(methodOn(NotificationController.class).buscarNotificacion(id)).withSelfRel());
            return ResponseEntity.ok(model);
        }).orElseGet(() -> ResponseEntity.notFound().build());
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
package com.DragonForge.rng_service.controller;

import com.DragonForge.rng_service.dto.RollResultDTO;
import com.DragonForge.rng_service.model.Tirada;
import com.DragonForge.rng_service.service.DiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/dice")
@Tag(name = "Tiradas de Dados (RNG)", description = "Motor matematico para simular tiradas de dados virtuales (d4, d6, d8, d20...) y mantener el registro del azar")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operacion realizada correctamente"),
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "204", description = "Solicitud procesada sin contenido"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado")
})
public class DiceController {

    @Autowired
    private DiceService diceService;

    @Operation(summary = "Lanzar dados virtuales", description = "Simula una tirada indicando cantidad de caras, numero de dados y modificador final.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tirada ejecutada correctamente", content = @Content(schema = @Schema(implementation = RollResultDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parametros de tirada invalidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/roll")
    public ResponseEntity<EntityModel<RollResultDTO>> rollDice(
            @Parameter(description = "Cantidad de caras del dado", example = "20") @RequestParam(defaultValue = "20") int sides,
            @Parameter(description = "Cantidad de dados a lanzar", example = "1") @RequestParam(defaultValue = "1") int count,
            @Parameter(description = "Modificador que se suma al total", example = "2") @RequestParam(defaultValue = "0") int modifier) {

        RollResultDTO result = diceService.roll(sides, count, modifier);
        EntityModel<RollResultDTO> model = EntityModel.of(result);
        model.add(linkTo(methodOn(DiceController.class).rollDice(sides, count, modifier)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Consultar historial de tiradas", description = "Retorna las tiradas registradas recientemente por el servicio RNG.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Tirada.class)))),
            @ApiResponse(responseCode = "204", description = "No existen tiradas registradas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/historial")
    public ResponseEntity<CollectionModel<Tirada>> verHistorial() {
        List<Tirada> historial = diceService.obtenerHistorial();
        if(historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<Tirada> model = CollectionModel.of(historial);
        model.add(linkTo(methodOn(DiceController.class).verHistorial()).withSelfRel());
        return ResponseEntity.ok(model);
    }
}

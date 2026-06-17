package com.DragonForge.rng_service.controller;

import com.DragonForge.rng_service.dto.RollResultDTO;
import com.DragonForge.rng_service.model.Tirada;
import com.DragonForge.rng_service.service.DiceService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Tiradas de Dados (RNG)", description = "Motor matemático para simular tiradas de dados virtuales (d4, d6, d8, d20...) y mantener el registro del azar")
public class DiceController {

    @Autowired
    private DiceService diceService;

    @Operation(summary = "Lanza uno o múltiples dados virtuales especificando la cantidad de caras (ej: 20), el número de dados y el modificador (+2, -1, etc.)")
    @GetMapping("/roll")
    public ResponseEntity<EntityModel<RollResultDTO>> rollDice(
            @RequestParam(defaultValue = "20") int sides,
            @RequestParam(defaultValue = "1") int count,
            @RequestParam(defaultValue = "0") int modifier) {

        RollResultDTO result = diceService.roll(sides, count, modifier);
        EntityModel<RollResultDTO> model = EntityModel.of(result);
        model.add(linkTo(methodOn(DiceController.class).rollDice(sides, count, modifier)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Consulta el historial de las últimas tiradas realizadas, ideal para revisar pifias críticas o éxitos épicos")
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

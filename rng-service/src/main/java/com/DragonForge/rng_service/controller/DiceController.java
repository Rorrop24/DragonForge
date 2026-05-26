package com.DragonForge.rng_service.controller;

import com.DragonForge.rng_service.dto.RollResultDTO;
import com.DragonForge.rng_service.model.Tirada;
import com.DragonForge.rng_service.service.DiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dice")
public class DiceController {

    @Autowired
    private DiceService diceService;

    // Endpoint principal de tiradas
    // Ejemplo: GET http://localhost:8080/api/dice/roll?sides=10&count=1&modifier=5
    @GetMapping("/roll")
    public ResponseEntity<RollResultDTO> rollDice(
            @RequestParam(defaultValue = "20") int sides,
            @RequestParam(defaultValue = "1") int count,
            @RequestParam(defaultValue = "0") int modifier) {

        RollResultDTO result = diceService.roll(sides, count, modifier);
        return ResponseEntity.ok(result); // 200 OK
    }

    // Endpoint para ver el historial (Cumple requisito REST adicional)
    @GetMapping("/historial")
    public ResponseEntity<List<Tirada>> verHistorial() {
        List<Tirada> historial = diceService.obtenerHistorial();
        if(historial.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(historial);
    }
}
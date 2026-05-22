package com.DragonForge.character_service.controller;


import com.DragonForge.character_service.dto.PersonajeDTO;
import com.DragonForge.character_service.model.Personaje;
import com.DragonForge.character_service.service.PersonajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personajes")
public class PersonajeController {

    @Autowired
    private PersonajeService service;

    @GetMapping
    public ResponseEntity listar(){
        List list = service.listarTodos();
        if(list.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity busca(@PathVariable Integer id) {
        try {
            PersonajeDTO dto = service.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException exc) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity registrar (@Valid @RequestBody Personaje personaje){
        PersonajeDTO nuevito = service.crearPersonaje(personaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevito);
    }
}

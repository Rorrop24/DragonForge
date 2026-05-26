package com.DragonForge.rng_service.service;

import com.DragonForge.rng_service.dto.RollResultDTO;
import com.DragonForge.rng_service.model.Tirada;
import com.DragonForge.rng_service.repository.TiradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiceService {

    @Autowired
    private TiradaRepository repository;

    private final SecureRandom random = new SecureRandom();

    public RollResultDTO roll(int diceSides, int numberOfDice, int modifier) {
        List<Integer> rolls = new ArrayList<>();
        int total = modifier;

        // Tirar los dados uno por uno
        for (int i = 0; i < numberOfDice; i++) {
            int result = random.nextInt(diceSides) + 1;
            rolls.add(result);
            total += result;
        }

        // Preparar el DTO de respuesta
        RollResultDTO rollResult = new RollResultDTO();
        rollResult.setIndividualRolls(rolls);
        rollResult.setTotal(total);
        rollResult.setModifier(modifier);

        // Lógica de crítico solo si es un d20
        if (diceSides == 20 && numberOfDice == 1) {
            rollResult.setCriticalSuccess(rolls.get(0) == 20);
            rollResult.setCriticalFail(rolls.get(0) == 1);
        }

        // Guardar en el historial de la base de datos (Persistencia JPA)
        Tirada historial = new Tirada();
        historial.setCarasDado(diceSides);
        historial.setCantidadDados(numberOfDice);
        historial.setModificador(modifier);
        historial.setResultadoTotal(total);
        repository.save(historial);

        return rollResult;
    }

    // Método para consultar el historial (opcional, para el CRUD)
    public List<Tirada> obtenerHistorial() {
        return repository.findAll();
    }
}
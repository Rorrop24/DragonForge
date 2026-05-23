package com.DragonForge.log_service.service;

import com.DragonForge.log_service.model.Diario;
import com.DragonForge.log_service.model.Entrada;
import com.DragonForge.log_service.repository.DiarioRepository;
import com.DragonForge.log_service.repository.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private DiarioRepository diarioRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    public List<Diario> listarDiarios() {
        return diarioRepository.findAll();
    }

    public Optional<Diario> buscarDiario(Integer id) {
        return diarioRepository.findById(id);
    }

    public Diario crearDiario(Diario diario) {
        return diarioRepository.save(diario);
    }

    public List<Entrada> verEntradasDeDiario(Integer diarioId) {
        return entradaRepository.findByDiarioId(diarioId);
    }

    public Entrada agregarEntrada(Integer diarioId, Entrada entrada) {
        Diario diario = diarioRepository.findById(diarioId)
                .orElseThrow(() -> new RuntimeException("Diario no encontrado con ID: " + diarioId));

        entrada.setDiario(diario);
        return entradaRepository.save(entrada);
    }
}
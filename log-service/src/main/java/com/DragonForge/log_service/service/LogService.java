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
    public Diario actualizarDiario(Integer id, Diario datos) {
        Diario diario = diarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diario no encontrado con ID: " + id));

        diario.setNombreCampana(datos.getNombreCampana());
        diario.setDmAsignado(datos.getDmAsignado());
        return diarioRepository.save(diario);
    }

    public void eliminarDiario(Integer id) {
        if (!diarioRepository.existsById(id)) {
            throw new RuntimeException("Diario no encontrado con ID: " + id);
        }

        diarioRepository.deleteById(id);
    }

    public Optional<Entrada> buscarEntrada(Integer id) {
        return entradaRepository.findById(id);
    }

    public Entrada actualizarEntrada(Integer id, Entrada datos) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + id));

        entrada.setTipoEvento(datos.getTipoEvento());
        entrada.setDescripcion(datos.getDescripcion());

        if (datos.getDiario() != null && datos.getDiario().getId() != null) {
            Diario diario = diarioRepository.findById(datos.getDiario().getId())
                    .orElseThrow(() -> new RuntimeException("Diario no encontrado con ID: " + datos.getDiario().getId()));
            entrada.setDiario(diario);
        }

        return entradaRepository.save(entrada);
    }

    public void eliminarEntrada(Integer id) {
        if (!entradaRepository.existsById(id)) {
            throw new RuntimeException("Entrada no encontrada con ID: " + id);
        }

        entradaRepository.deleteById(id);
    }
}

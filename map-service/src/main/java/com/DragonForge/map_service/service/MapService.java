package com.DragonForge.map_service.service;

import com.DragonForge.map_service.model.Mapa;
import com.DragonForge.map_service.model.Ubicacion;
import com.DragonForge.map_service.repository.MapaRepository;
import com.DragonForge.map_service.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapService {

    @Autowired
    private MapaRepository mapaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public List<Mapa> listarMapas() {
        return mapaRepository.findAll();
    }

    public Optional<Mapa> buscarMapa(Integer id) {
        return mapaRepository.findById(id);
    }

    public Mapa crearMapa(Mapa mapa) {
        return mapaRepository.save(mapa);
    }

    public List<Ubicacion> listarUbicacionesDeMapa(Integer mapaId) {
        return ubicacionRepository.findByMapaId(mapaId);
    }

    public Ubicacion agregarUbicacion(Integer mapaId, Ubicacion ubicacion) {
        Mapa mapa = mapaRepository.findById(mapaId)
                .orElseThrow(() -> new RuntimeException("Mapa no encontrado con ID: " + mapaId));

        ubicacion.setMapa(mapa);
        return ubicacionRepository.save(ubicacion);
    }
}
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
    public Mapa actualizarMapa(Integer id, Mapa datos) {
        Mapa mapa = mapaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mapa no encontrado con ID: " + id));

        mapa.setNombre(datos.getNombre());
        mapa.setRegion(datos.getRegion());
        mapa.setDescripcion(datos.getDescripcion());
        return mapaRepository.save(mapa);
    }

    public void eliminarMapa(Integer id) {
        if (!mapaRepository.existsById(id)) {
            throw new RuntimeException("Mapa no encontrado con ID: " + id);
        }

        mapaRepository.deleteById(id);
    }

    public Optional<Ubicacion> buscarUbicacion(Integer id) {
        return ubicacionRepository.findById(id);
    }

    public Ubicacion actualizarUbicacion(Integer id, Ubicacion datos) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con ID: " + id));

        ubicacion.setNombre(datos.getNombre());
        ubicacion.setTipo(datos.getTipo());
        ubicacion.setNivelPeligro(datos.getNivelPeligro());

        if (datos.getMapa() != null && datos.getMapa().getId() != null) {
            Mapa mapa = mapaRepository.findById(datos.getMapa().getId())
                    .orElseThrow(() -> new RuntimeException("Mapa no encontrado con ID: " + datos.getMapa().getId()));
            ubicacion.setMapa(mapa);
        }

        return ubicacionRepository.save(ubicacion);
    }

    public void eliminarUbicacion(Integer id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new RuntimeException("Ubicacion no encontrada con ID: " + id);
        }

        ubicacionRepository.deleteById(id);
    }
}

package com.DragonForge.asset_service.service;

import com.DragonForge.asset_service.model.ArchivoActivo;
import com.DragonForge.asset_service.model.CarpetaActivo;
import com.DragonForge.asset_service.repository.ArchivoActivoRepository;
import com.DragonForge.asset_service.repository.CarpetaActivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    @Autowired
    private CarpetaActivoRepository carpetaRepository;

    @Autowired
    private ArchivoActivoRepository archivoRepository;

    public List<CarpetaActivo> listarCarpetas() {
        return carpetaRepository.findAll();
    }

    public Optional<CarpetaActivo> buscarCarpeta(Integer id) {
        return carpetaRepository.findById(id);
    }

    public CarpetaActivo crearCarpeta(CarpetaActivo carpeta) {
        return carpetaRepository.save(carpeta);
    }

    public List<ArchivoActivo> listarArchivosDeCarpeta(Integer carpetaId) {
        return archivoRepository.findByCarpetaId(carpetaId);
    }

    public ArchivoActivo guardarArchivo(Integer carpetaId, ArchivoActivo archivo) {
        CarpetaActivo carpeta = carpetaRepository.findById(carpetaId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada con ID: " + carpetaId));

        archivo.setCarpeta(carpeta);
        return archivoRepository.save(archivo);
    }
}
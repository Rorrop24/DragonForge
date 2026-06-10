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

    public CarpetaActivo actualizarCarpeta(Integer id, CarpetaActivo datos) {
        CarpetaActivo carpeta = carpetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada con ID: " + id));

        carpeta.setNombre(datos.getNombre());
        carpeta.setDescripcion(datos.getDescripcion());
        return carpetaRepository.save(carpeta);
    }

    public void eliminarCarpeta(Integer id) {
        if (!carpetaRepository.existsById(id)) {
            throw new RuntimeException("Carpeta no encontrada con ID: " + id);
        }

        carpetaRepository.deleteById(id);
    }

    public List<ArchivoActivo> listarArchivosDeCarpeta(Integer carpetaId) {
        return archivoRepository.findByCarpetaId(carpetaId);
    }

    public Optional<ArchivoActivo> buscarArchivo(Integer id) {
        return archivoRepository.findById(id);
    }

    public ArchivoActivo guardarArchivo(Integer carpetaId, ArchivoActivo archivo) {
        CarpetaActivo carpeta = carpetaRepository.findById(carpetaId)
                .orElseThrow(() -> new RuntimeException("Carpeta no encontrada con ID: " + carpetaId));

        archivo.setCarpeta(carpeta);
        return archivoRepository.save(archivo);
    }

    public ArchivoActivo actualizarArchivo(Integer id, ArchivoActivo datos) {
        ArchivoActivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con ID: " + id));

        archivo.setNombreArchivo(datos.getNombreArchivo());
        archivo.setUrlRecurso(datos.getUrlRecurso());
        archivo.setTipoMime(datos.getTipoMime());
        archivo.setPesoKb(datos.getPesoKb());

        if (datos.getCarpeta() != null && datos.getCarpeta().getId() != null) {
            CarpetaActivo carpeta = carpetaRepository.findById(datos.getCarpeta().getId())
                    .orElseThrow(() -> new RuntimeException("Carpeta no encontrada con ID: " + datos.getCarpeta().getId()));
            archivo.setCarpeta(carpeta);
        }

        return archivoRepository.save(archivo);
    }

    public void eliminarArchivo(Integer id) {
        if (!archivoRepository.existsById(id)) {
            throw new RuntimeException("Archivo no encontrado con ID: " + id);
        }

        archivoRepository.deleteById(id);
    }
}

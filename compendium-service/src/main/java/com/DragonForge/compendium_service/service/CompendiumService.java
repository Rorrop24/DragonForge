package com.DragonForge.compendium_service.service;

import com.DragonForge.compendium_service.model.CategoriaCompendio;
import com.DragonForge.compendium_service.model.EntradaCompendio;
import com.DragonForge.compendium_service.repository.CategoriaCompendioRepository;
import com.DragonForge.compendium_service.repository.EntradaCompendioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompendiumService {

    @Autowired
    private CategoriaCompendioRepository categoriaRepository;

    @Autowired
    private EntradaCompendioRepository entradaRepository;

    public List<CategoriaCompendio> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public List<EntradaCompendio> listarTodasLasEntradas() {
        return entradaRepository.findAll();
    }

    public Optional<EntradaCompendio> buscarEntradaPorId(Integer id) {
        return entradaRepository.findById(id);
    }

    public List<EntradaCompendio> buscarEntradasPorCategoria(Integer categoriaId) {
        return entradaRepository.findByCategoriaId(categoriaId);
    }

    public CategoriaCompendio crearCategoria(CategoriaCompendio categoria) {
        return categoriaRepository.save(categoria);
    }

    public EntradaCompendio agregarEntrada(Integer categoriaId, EntradaCompendio entrada) {
        CategoriaCompendio categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));

        entrada.setCategoria(categoria);
        return entradaRepository.save(entrada);
    }
}
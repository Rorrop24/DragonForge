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
    public CategoriaCompendio actualizarCategoria(Integer id, CategoriaCompendio datos) {
        CategoriaCompendio categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + id));

        categoria.setNombre(datos.getNombre());
        categoria.setDescripcion(datos.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria no encontrada con ID: " + id);
        }

        categoriaRepository.deleteById(id);
    }

    public EntradaCompendio actualizarEntrada(Integer id, EntradaCompendio datos) {
        EntradaCompendio entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + id));

        entrada.setTitulo(datos.getTitulo());
        entrada.setDescripcionDetallada(datos.getDescripcionDetallada());
        entrada.setEstadisticas(datos.getEstadisticas());

        if (datos.getCategoria() != null && datos.getCategoria().getId() != null) {
            CategoriaCompendio categoria = categoriaRepository.findById(datos.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + datos.getCategoria().getId()));
            entrada.setCategoria(categoria);
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

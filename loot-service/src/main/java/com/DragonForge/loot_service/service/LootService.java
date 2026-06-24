package com.DragonForge.loot_service.service;

import com.DragonForge.loot_service.model.Categoria;
import com.DragonForge.loot_service.model.Item;
import com.DragonForge.loot_service.repository.CategoriaRepository;
import com.DragonForge.loot_service.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LootService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Item> listarTodosLosItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> buscarItemPorId(Integer id) {
        return itemRepository.findById(id);
    }

    public List<Item> buscarItemsPorCategoria(Integer categoriaId) {
        return itemRepository.findByCategoriaId(categoriaId);
    }

    public Item guardarItem(Item item) {
        Integer catId = item.getCategoria().getId();
        Categoria categoriaExiste = categoriaRepository.findById(catId)
                .orElseThrow(() -> new RuntimeException("La categoría con ID " + catId + " no existe."));

        item.setCategoria(categoriaExiste);
        return itemRepository.save(item);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
    public Item actualizarItem(Integer id, Item datos) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con ID: " + id));

        item.setNombre(datos.getNombre());
        item.setRareza(datos.getRareza());
        item.setPeso(datos.getPeso());
        item.setValorOro(datos.getValorOro());
        item.setDanioOEfecto(datos.getDanioOEfecto());

        if (datos.getCategoria() != null && datos.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(datos.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + datos.getCategoria().getId()));
            item.setCategoria(categoria);
        }

        return itemRepository.save(item);
    }

    public void eliminarItem(Integer id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item no encontrado con ID: " + id);
        }

        itemRepository.deleteById(id);
    }

    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> buscarCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria actualizarCategoria(Integer id, Categoria datos) {
        Categoria categoria = categoriaRepository.findById(id)
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
}

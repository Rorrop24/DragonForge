package com.DragonForge.wiki_service.service;

import com.DragonForge.wiki_service.model.Articulo;
import com.DragonForge.wiki_service.model.Comentario;
import com.DragonForge.wiki_service.repository.ArticuloRepository;
import com.DragonForge.wiki_service.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WikiService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    public List<Articulo> listarArticulos() {
        return articuloRepository.findAll();
    }

    public Optional<Articulo> buscarArticulo(Integer id) {
        return articuloRepository.findById(id);
    }

    public Articulo crearArticulo(Articulo articulo) {
        return articuloRepository.save(articulo);
    }

    public List<Comentario> verComentariosDeArticulo(Integer articuloId) {
        return comentarioRepository.findByArticuloId(articuloId);
    }

    public Comentario agregarComentario(Integer articuloId, Comentario comentario) {
        Articulo articulo = articuloRepository.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        comentario.setArticulo(articulo);
        return comentarioRepository.save(comentario);
    }
}
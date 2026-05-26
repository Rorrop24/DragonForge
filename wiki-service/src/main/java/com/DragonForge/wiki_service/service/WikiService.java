package com.DragonForge.wiki_service.service;

import com.DragonForge.wiki_service.client.UserClient;
import com.DragonForge.wiki_service.dto.UsuarioDTO;
import com.DragonForge.wiki_service.model.Articulo;
import com.DragonForge.wiki_service.model.Comentario;
import com.DragonForge.wiki_service.repository.ArticuloRepository;
import com.DragonForge.wiki_service.repository.ComentarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WikiService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;


    @Autowired
    private UserClient userClient;

    public List<Articulo> listarArticulos() {
        log.info("Consultando la lista completa de artículos de la wiki...");
        return articuloRepository.findAll();
    }

    public Optional<Articulo> buscarArticulo(Integer id) {
        log.info("Buscando artículo en la wiki con ID: {}", id);
        return articuloRepository.findById(id);
    }


    public Articulo crearArticulo(Articulo articulo, Integer autorId) {
        log.info("Iniciando publicación de artículo. Verificando autor con ID: {}", autorId);

        UsuarioDTO autorReal = userClient.obtenerUsuarioPorId(autorId);

        articulo.setAutor(autorReal.getUsername());
        log.info("Autor validado exitosamente. Asignando autor: {}", autorReal.getUsername());

        Articulo guardado = articuloRepository.save(articulo);
        log.info("Artículo '{}' publicado con éxito con ID: {}", guardado.getTitulo(), guardado.getId());

        return guardado;
    }

    public List<Comentario> verComentariosDeArticulo(Integer articuloId) {
        log.info("Consultando comentarios para el artículo con ID: {}", articuloId);
        return comentarioRepository.findByArticuloId(articuloId);
    }

    public Comentario agregarComentario(Integer articuloId, Comentario comentario) {
        log.info("Intentando agregar un comentario al artículo con ID: {}", articuloId);

        Articulo articulo = articuloRepository.findById(articuloId)
                .orElseThrow(() -> {
                    log.error("Error al comentar: No se encontró el artículo con ID {}", articuloId);
                    return new RuntimeException("Artículo no encontrado");
                });

        comentario.setArticulo(articulo);
        Comentario guardado = comentarioRepository.save(comentario);

        log.info("Comentario agregado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }
}
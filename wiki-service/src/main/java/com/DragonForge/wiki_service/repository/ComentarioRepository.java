package com.DragonForge.wiki_service.repository;

import com.DragonForge.wiki_service.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByArticuloId(Integer articuloId);
}
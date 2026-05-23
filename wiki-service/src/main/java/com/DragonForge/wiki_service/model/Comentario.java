package com.DragonForge.wiki_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String texto;

    @NotBlank(message = "El autor del comentario es obligatorio")
    @Column(name = "autor_comentario")
    private String autorComentario;

    @Column(name = "fecha_comentario")
    private LocalDateTime fechaComentario = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;
}
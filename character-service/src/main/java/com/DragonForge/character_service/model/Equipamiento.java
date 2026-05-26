package com.DragonForge.character_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipamientos")
public class Equipamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del item es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El tipo de item es obligatorio")
    private String tipo;

    @NotNull
    @Positive
    private Integer cantidad = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personaje_id", nullable = false)
    private Personaje personaje;
}
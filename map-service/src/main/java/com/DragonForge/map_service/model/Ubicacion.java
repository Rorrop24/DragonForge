package com.DragonForge.map_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ubicaciones")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la ubicación es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo es obligatorio (ej: Ciudad, Mazmorra, Bosque)")
    private String tipo;

    @NotNull(message = "El nivel de peligro es obligatorio")
    @Min(value = 1, message = "El nivel de peligro mínimo es 1")
    @Max(value = 20, message = "El nivel de peligro máximo es 20")
    @Column(name = "nivel_peligro")
    private Integer nivelPeligro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mapa_id", nullable = false)
    private Mapa mapa;
}
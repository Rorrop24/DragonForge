package com.DragonForge.loot_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del item es obligatorio")
    private String nombre;

    @NotBlank(message = "La rareza es obligatoria")
    private String rareza;

    @NotNull
    @PositiveOrZero(message = "El peso no puede ser negativo")
    private Double peso;

    @NotNull
    @PositiveOrZero(message = "El valor en oro no puede ser negativo")
    @Column(name = "valor_oro")
    private Integer valorOro;

    @Column(name = "danio_o_efecto")
    private String danioOEfecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
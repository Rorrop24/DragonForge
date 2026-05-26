package com.DragonForge.rng_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "historial_tiradas")
public class Tirada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(2)
    @Column(name = "caras_dado")
    private Integer carasDado;

    @NotNull
    @Min(1)
    @Column(name = "cantidad_dados")
    private Integer cantidadDados;

    @NotNull
    private Integer modificador;

    @NotNull
    @Column(name = "resultado_total")
    private Integer resultadoTotal;

    @Column(name = "fecha_tirada")
    private LocalDateTime fechaTirada = LocalDateTime.now();
}
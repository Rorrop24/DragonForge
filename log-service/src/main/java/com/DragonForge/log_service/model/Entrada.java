package com.DragonForge.log_service.model;

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
@Table(name = "entradas")
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El tipo de evento es obligatorio (ej: Combate, Diálogo, Exploración)")
    @Column(name = "tipo_evento")
    private String tipoEvento;

    @NotBlank(message = "La descripción del evento no puede estar vacía")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_evento")
    private LocalDateTime fechaEvento = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diario_id", nullable = false)
    private Diario diario;
}
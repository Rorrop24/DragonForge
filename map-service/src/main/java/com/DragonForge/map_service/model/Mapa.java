package com.DragonForge.map_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mapas")
public class Mapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del mapa es obligatorio")
    @Column(unique = true, nullable = false)
    private String nombre;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    private String descripcion;

    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Ubicacion> ubicaciones;
}
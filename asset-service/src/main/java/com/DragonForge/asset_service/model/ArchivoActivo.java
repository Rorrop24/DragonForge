package com.DragonForge.asset_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "archivos")
public class ArchivoActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @NotBlank(message = "La URL del recurso es obligatoria")
    @Column(name = "url_recurso", nullable = false)
    private String urlRecurso;

    @NotBlank(message = "El tipo MIME es obligatorio (ej: image/png)")
    @Column(name = "tipo_mime", nullable = false)
    private String tipoMime;

    @NotNull(message = "El peso del archivo es obligatorio")
    @Column(name = "peso_kb", nullable = false)
    private Double pesoKb;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carpeta_id", nullable = false)
    private CarpetaActivo carpeta;
}
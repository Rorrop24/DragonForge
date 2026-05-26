package com.DragonForge.character_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PersonajeDTO {
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombrePersonaje;

    @NotBlank(message = "La raza es requerida")
    private String raza;

    @NotBlank(message = "La clase es requerida")
    private String clase;

    @Min(1) @Max(20)
    private Integer nivel;

    private Integer puntosGolpe;
    private String trasfondo;
    private Boolean vivo;

    private List<EquipamientoDTO> inventario;
}
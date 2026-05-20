package com.DragonForge.character_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EquipamientoDTO {
    private Integer id;
    @NotBlank(message = "El nombre del objeto es requerido")
    private String nombre;
    @NotBlank(message = "El tipo de objeto es requerido")
    private String tipo;
    @Positive
    private Integer cantidad;
}
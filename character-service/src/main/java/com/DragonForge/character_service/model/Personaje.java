package com.DragonForge.character_service.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personajes")
public class Personaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre_personaje", nullable = false, length = 100)
    private String nombrePersonaje;
    @NotBlank(message = "La raza es obligatoria")
    @Column(nullable = false)
    private String raza;
    @NotBlank(message = "La clase es obligatoria")
    @Column(nullable = false)
    private String clase;
    @NotNull
    @Min(1) @Max(20)
    private Integer nivel = 1;
    @NotNull
    @PositiveOrZero
    @Column(name = "puntos_golpe")
    private Integer puntosGolpe;
    private String trasfondo;
    private Boolean vivo = true;
    @OneToMany(mappedBy = "personaje", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List inventario = new ArrayList<>();
}
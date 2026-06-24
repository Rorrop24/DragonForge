package com.DragonForge.map_service.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-13: crear mapa con ubicacion usando datos ficticios")
    void shouldCreateMapWithLocationUsingFakerData() {
        // Arrange
        Mapa mapa = crearMapa();
        Ubicacion ubicacion = crearUbicacion(mapa);
        mapa.setUbicaciones(List.of(ubicacion));

        // Act
        Set<ConstraintViolation<Mapa>> mapaViolations = validator.validate(mapa);
        Set<ConstraintViolation<Ubicacion>> ubicacionViolations = validator.validate(ubicacion);

        // Assert
        assertNotNull(mapa);
        assertTrue(mapaViolations.isEmpty());
        assertTrue(ubicacionViolations.isEmpty());
        assertSame(mapa, ubicacion.getMapa());
        assertEquals(1, mapa.getUbicaciones().size());
    }

    @Test
    @DisplayName("CP-14: rechazar ubicacion con peligro mayor a 20")
    void shouldRejectLocationWithDangerLevelAboveTwenty() {
        // Arrange
        Ubicacion ubicacion = crearUbicacion(crearMapa());
        ubicacion.setNivelPeligro(21);

        // Act
        Set<ConstraintViolation<Ubicacion>> violations = validator.validate(ubicacion);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("nivelPeligro", violations.iterator().next().getPropertyPath().toString());
    }

    private Mapa crearMapa() {
        Mapa mapa = new Mapa();
        mapa.setNombre(faker.address().cityName());
        mapa.setRegion(faker.address().state());
        mapa.setDescripcion(faker.lorem().sentence());
        return mapa;
    }

    private Ubicacion crearUbicacion(Mapa mapa) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombre(faker.address().cityName());
        ubicacion.setTipo(faker.options().option("Ciudad", "Mazmorra", "Bosque", "Ruinas"));
        ubicacion.setNivelPeligro(faker.number().numberBetween(1, 21));
        ubicacion.setMapa(mapa);
        return ubicacion;
    }
}

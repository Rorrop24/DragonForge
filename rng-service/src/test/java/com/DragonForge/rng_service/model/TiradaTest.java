package com.DragonForge.rng_service.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TiradaTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-11: crear historial de tirada usando datos ficticios")
    void shouldCreateValidDiceHistoryUsingFakerData() {
        // Arrange
        Tirada tirada = new Tirada();
        tirada.setCarasDado(faker.options().option(4, 6, 8, 10, 12, 20));
        tirada.setCantidadDados(faker.number().numberBetween(1, 8));
        tirada.setModificador(faker.number().numberBetween(-2, 10));
        tirada.setResultadoTotal(faker.number().numberBetween(1, 80));

        // Act
        Set<ConstraintViolation<Tirada>> violations = validator.validate(tirada);

        // Assert
        assertNotNull(tirada);
        assertNotNull(tirada.getFechaTirada());
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("CP-12: rechazar tirada con dado de una cara")
    void shouldRejectDiceRollWithInvalidDiceSides() {
        // Arrange
        Tirada tirada = new Tirada();
        tirada.setCarasDado(1);
        tirada.setCantidadDados(faker.number().numberBetween(1, 8));
        tirada.setModificador(faker.number().numberBetween(-2, 10));
        tirada.setResultadoTotal(faker.number().numberBetween(1, 80));

        // Act
        Set<ConstraintViolation<Tirada>> violations = validator.validate(tirada);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("carasDado", violations.iterator().next().getPropertyPath().toString());
    }
}

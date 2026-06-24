package com.DragonForge.log_service.model;

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

class LogModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-19: crear diario con entrada usando datos ficticios")
    void shouldCreateLogWithEntryUsingFakerData() {
        // Arrange
        Diario diario = crearDiario();
        Entrada entrada = crearEntrada(diario);
        diario.setEntradas(List.of(entrada));

        // Act
        Set<ConstraintViolation<Diario>> diarioViolations = validator.validate(diario);
        Set<ConstraintViolation<Entrada>> entradaViolations = validator.validate(entrada);

        // Assert
        assertNotNull(diario);
        assertNotNull(diario.getFechaCreacion());
        assertNotNull(entrada.getFechaEvento());
        assertTrue(diarioViolations.isEmpty());
        assertTrue(entradaViolations.isEmpty());
        assertSame(diario, entrada.getDiario());
    }

    @Test
    @DisplayName("CP-20: rechazar entrada sin tipo de evento")
    void shouldRejectEntryWithoutEventType() {
        // Arrange
        Entrada entrada = crearEntrada(crearDiario());
        entrada.setTipoEvento("");

        // Act
        Set<ConstraintViolation<Entrada>> violations = validator.validate(entrada);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("tipoEvento", violations.iterator().next().getPropertyPath().toString());
    }

    private Diario crearDiario() {
        Diario diario = new Diario();
        diario.setNombreCampana(faker.book().title());
        diario.setDmAsignado(faker.name().fullName());
        return diario;
    }

    private Entrada crearEntrada(Diario diario) {
        Entrada entrada = new Entrada();
        entrada.setTipoEvento(faker.options().option("Combate", "Dialogo", "Exploracion"));
        entrada.setDescripcion(faker.lorem().paragraph());
        entrada.setDiario(diario);
        return entrada;
    }
}

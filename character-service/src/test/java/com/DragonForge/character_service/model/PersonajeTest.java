package com.DragonForge.character_service.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonajeTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-03: crear personaje con inventario usando datos ficticios")
    void shouldCreateCharacterWithEquipmentUsingFakerData() {
        // Arrange
        Personaje personaje = new Personaje();
        personaje.setNombrePersonaje(faker.name().firstName());
        personaje.setRaza(faker.options().option("Humano", "Elfo", "Enano", "Tiefling"));
        personaje.setClase(faker.options().option("Guerrero", "Mago", "Picaro", "Clerigo"));
        personaje.setNivel(faker.number().numberBetween(1, 20));
        personaje.setPuntosGolpe(faker.number().numberBetween(1, 150));
        personaje.setTrasfondo(faker.lorem().sentence());
        personaje.setInventario(new ArrayList<>());

        Equipamiento equipamiento = new Equipamiento();
        equipamiento.setNombre(faker.commerce().productName());
        equipamiento.setTipo(faker.options().option("Arma", "Armadura", "Consumible"));
        equipamiento.setCantidad(faker.number().numberBetween(1, 5));
        equipamiento.setPersonaje(personaje);

        // Act
        personaje.getInventario().add(equipamiento);
        Set<ConstraintViolation<Personaje>> personajeViolations = validator.validate(personaje);
        Set<ConstraintViolation<Equipamiento>> equipamientoViolations = validator.validate(equipamiento);

        // Assert
        assertNotNull(personaje);
        assertTrue(personajeViolations.isEmpty());
        assertTrue(equipamientoViolations.isEmpty());
        assertEquals(1, personaje.getInventario().size());
        assertSame(personaje, personaje.getInventario().get(0).getPersonaje());
    }
}

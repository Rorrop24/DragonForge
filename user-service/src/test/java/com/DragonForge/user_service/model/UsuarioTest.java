package com.DragonForge.user_service.model;

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

class UsuarioTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-01: registrar usuario con datos ficticios validos")
    void shouldCreateValidUsuarioWithFakerData() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setUsername(faker.name().username());
        usuario.setEmail(faker.internet().emailAddress());
        usuario.setPassword(faker.internet().password(8, 20));

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertNotNull(usuario);
        assertNotNull(usuario.getFechaRegistro());
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("CP-02: rechazar correo de usuario con formato invalido")
    void shouldRejectInvalidEmailFormat() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setUsername(faker.name().username());
        usuario.setEmail("correo-invalido");
        usuario.setPassword(faker.internet().password(8, 20));

        // Act
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }
}

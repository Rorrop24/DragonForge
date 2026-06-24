package com.DragonForge.notification_service.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-21: crear buzon con notificacion usando datos ficticios")
    void shouldCreateMailboxWithNotificationUsingFakerData() {
        // Arrange
        Buzon buzon = crearBuzon();
        Notificacion notificacion = crearNotificacion(buzon);
        buzon.setNotificaciones(List.of(notificacion));

        // Act
        Set<ConstraintViolation<Buzon>> buzonViolations = validator.validate(buzon);
        Set<ConstraintViolation<Notificacion>> notificacionViolations = validator.validate(notificacion);

        // Assert
        assertNotNull(buzon);
        assertFalse(notificacion.getLeida());
        assertTrue(buzonViolations.isEmpty());
        assertTrue(notificacionViolations.isEmpty());
        assertSame(buzon, notificacion.getBuzon());
    }

    @Test
    @DisplayName("CP-22: rechazar notificacion sin mensaje")
    void shouldRejectNotificationWithoutMessage() {
        // Arrange
        Notificacion notificacion = crearNotificacion(crearBuzon());
        notificacion.setMensaje("");

        // Act
        Set<ConstraintViolation<Notificacion>> violations = validator.validate(notificacion);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("mensaje", violations.iterator().next().getPropertyPath().toString());
    }

    private Buzon crearBuzon() {
        Buzon buzon = new Buzon();
        buzon.setUsuarioId(faker.number().numberBetween(1, 500));
        buzon.setNombreJugador(faker.name().username());
        return buzon;
    }

    private Notificacion crearNotificacion(Buzon buzon) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo(faker.lorem().sentence(3));
        notificacion.setMensaje(faker.lorem().sentence());
        notificacion.setBuzon(buzon);
        return notificacion;
    }
}

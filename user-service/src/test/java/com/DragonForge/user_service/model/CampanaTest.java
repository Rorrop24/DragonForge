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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CampanaTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-06: crear campana con dungeon master usando datos ficticios")
    void shouldCreateCampaignWithDungeonMasterUsingFakerData() {
        // Arrange
        Usuario dungeonMaster = new Usuario();
        dungeonMaster.setUsername(faker.name().username());
        dungeonMaster.setEmail(faker.internet().emailAddress());
        dungeonMaster.setPassword(faker.internet().password(8, 20));

        Campana campana = new Campana();
        campana.setNombre(faker.book().title());
        campana.setDescripcion(faker.lorem().sentence());
        campana.setDungeonMaster(dungeonMaster);

        // Act
        Set<ConstraintViolation<Campana>> campanaViolations = validator.validate(campana);
        Set<ConstraintViolation<Usuario>> usuarioViolations = validator.validate(dungeonMaster);

        // Assert
        assertNotNull(campana);
        assertTrue(campanaViolations.isEmpty());
        assertTrue(usuarioViolations.isEmpty());
        assertSame(dungeonMaster, campana.getDungeonMaster());
    }

    @Test
    @DisplayName("CP-07: rechazar campana sin nombre")
    void shouldRejectCampaignWithoutName() {
        // Arrange
        Usuario dungeonMaster = new Usuario();
        dungeonMaster.setUsername(faker.name().username());
        dungeonMaster.setEmail(faker.internet().emailAddress());
        dungeonMaster.setPassword(faker.internet().password(8, 20));

        Campana campana = new Campana();
        campana.setNombre("");
        campana.setDescripcion(faker.lorem().sentence());
        campana.setDungeonMaster(dungeonMaster);

        // Act
        Set<ConstraintViolation<Campana>> violations = validator.validate(campana);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("nombre", violations.iterator().next().getPropertyPath().toString());
    }
}

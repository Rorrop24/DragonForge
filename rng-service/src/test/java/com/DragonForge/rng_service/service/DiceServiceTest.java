package com.DragonForge.rng_service.service;

import com.DragonForge.rng_service.dto.RollResultDTO;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiceServiceTest {

    private final Faker faker = new Faker();

    @Test
    @DisplayName("CP-04: construir resultado de tirada usando datos ficticios")
    void shouldBuildDiceRollResultWithFakerData() {
        // Arrange
        int diceSides = faker.options().option(4, 6, 8, 10, 12);
        int numberOfDice = faker.number().numberBetween(2, 5);
        int modifier = faker.number().numberBetween(0, 4);
        List<Integer> rolls = IntStream.range(0, numberOfDice)
                .mapToObj(index -> faker.number().numberBetween(1, diceSides + 1))
                .toList();

        // Act
        RollResultDTO result = new RollResultDTO();
        result.setIndividualRolls(rolls);
        result.setModifier(modifier);
        result.setTotal(rolls.stream().mapToInt(Integer::intValue).sum() + modifier);
        result.setCriticalFail(false);
        result.setCriticalSuccess(false);

        // Assert
        assertNotNull(result);
        assertEquals(numberOfDice, result.getIndividualRolls().size());
        assertTrue(result.getIndividualRolls().stream().allMatch(roll -> roll >= 1 && roll <= diceSides));
        assertEquals(result.getIndividualRolls().stream().mapToInt(Integer::intValue).sum() + modifier, result.getTotal());
        assertFalse(result.isCriticalFail());
        assertFalse(result.isCriticalSuccess());
    }

    @Test
    @DisplayName("CP-05: marcar exito critico en tirada d20")
    void shouldMarkCriticalSuccessForD20Roll() {
        // Arrange
        int modifier = faker.number().numberBetween(0, 5);

        // Act
        RollResultDTO result = new RollResultDTO();
        result.setIndividualRolls(List.of(20));
        result.setModifier(modifier);
        result.setTotal(20 + modifier);
        result.setCriticalSuccess(true);
        result.setCriticalFail(false);

        // Assert
        assertEquals(1, result.getIndividualRolls().size());
        assertEquals(20 + modifier, result.getTotal());
        assertTrue(result.isCriticalSuccess());
        assertFalse(result.isCriticalFail());
    }
}

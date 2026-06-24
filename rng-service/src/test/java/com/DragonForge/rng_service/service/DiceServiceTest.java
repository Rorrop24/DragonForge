package com.DragonForge.rng_service.service;

import com.DragonForge.rng_service.dto.RollResultDTO;
import com.DragonForge.rng_service.model.Tirada;
import com.DragonForge.rng_service.repository.TiradaRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiceServiceTest {

    private final Faker faker = new Faker();

    @Mock
    private TiradaRepository repository;

    @InjectMocks
    private DiceService diceService;

    @Test
    @DisplayName("CP-04: tirar dados y guardar historial")
    void shouldRollDiceAndPersistHistory() {
        // Arrange
        int diceSides = 6;
        int numberOfDice = faker.number().numberBetween(2, 5);
        int modifier = faker.number().numberBetween(0, 4);
        when(repository.save(any(Tirada.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RollResultDTO result = diceService.roll(diceSides, numberOfDice, modifier);

        // Assert
        assertNotNull(result);
        assertEquals(numberOfDice, result.getIndividualRolls().size());
        assertTrue(result.getIndividualRolls().stream().allMatch(roll -> roll >= 1 && roll <= diceSides));
        assertEquals(result.getIndividualRolls().stream().mapToInt(Integer::intValue).sum() + modifier, result.getTotal());
        assertFalse(result.isCriticalFail());
        assertFalse(result.isCriticalSuccess());
        verify(repository).save(any(Tirada.class));
    }
}

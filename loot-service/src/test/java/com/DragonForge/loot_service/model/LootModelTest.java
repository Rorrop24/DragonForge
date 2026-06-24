package com.DragonForge.loot_service.model;

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

class LootModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-08: crear item vinculado a categoria usando datos ficticios")
    void shouldCreateItemWithCategoryUsingFakerData() {
        // Arrange
        Categoria categoria = crearCategoria();
        Item item = crearItem(categoria);
        categoria.setItems(List.of(item));

        // Act
        Set<ConstraintViolation<Categoria>> categoriaViolations = validator.validate(categoria);
        Set<ConstraintViolation<Item>> itemViolations = validator.validate(item);

        // Assert
        assertNotNull(item);
        assertTrue(categoriaViolations.isEmpty());
        assertTrue(itemViolations.isEmpty());
        assertSame(categoria, item.getCategoria());
        assertEquals(1, categoria.getItems().size());
    }

    @Test
    @DisplayName("CP-09: rechazar item con peso negativo")
    void shouldRejectItemWithNegativeWeight() {
        // Arrange
        Categoria categoria = crearCategoria();
        Item item = crearItem(categoria);
        item.setPeso(-1.0);

        // Act
        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("peso", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    @DisplayName("CP-10: rechazar categoria sin nombre")
    void shouldRejectCategoryWithoutName() {
        // Arrange
        Categoria categoria = crearCategoria();
        categoria.setNombre("");

        // Act
        Set<ConstraintViolation<Categoria>> violations = validator.validate(categoria);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("nombre", violations.iterator().next().getPropertyPath().toString());
    }

    private Categoria crearCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre(faker.options().option("Armas", "Armaduras", "Consumibles", "Reliquias"));
        categoria.setDescripcion(faker.lorem().sentence());
        return categoria;
    }

    private Item crearItem(Categoria categoria) {
        Item item = new Item();
        item.setNombre(faker.commerce().productName());
        item.setRareza(faker.options().option("Comun", "Raro", "Epico", "Legendario"));
        item.setPeso(faker.number().randomDouble(2, 0, 20));
        item.setValorOro(faker.number().numberBetween(0, 5000));
        item.setDanioOEfecto(faker.lorem().sentence());
        item.setCategoria(categoria);
        return item;
    }
}

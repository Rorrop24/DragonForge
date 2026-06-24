package com.DragonForge.compendium_service.model;

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

class CompendiumModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-17: crear entrada de compendio asociada a categoria")
    void shouldCreateCompendiumEntryWithCategoryUsingFakerData() {
        // Arrange
        CategoriaCompendio categoria = crearCategoria();
        EntradaCompendio entrada = crearEntrada(categoria);
        categoria.setEntradas(List.of(entrada));

        // Act
        Set<ConstraintViolation<CategoriaCompendio>> categoriaViolations = validator.validate(categoria);
        Set<ConstraintViolation<EntradaCompendio>> entradaViolations = validator.validate(entrada);

        // Assert
        assertNotNull(entrada);
        assertTrue(categoriaViolations.isEmpty());
        assertTrue(entradaViolations.isEmpty());
        assertSame(categoria, entrada.getCategoria());
        assertEquals(1, categoria.getEntradas().size());
    }

    @Test
    @DisplayName("CP-18: rechazar entrada sin descripcion detallada")
    void shouldRejectEntryWithoutDetailedDescription() {
        // Arrange
        EntradaCompendio entrada = crearEntrada(crearCategoria());
        entrada.setDescripcionDetallada("");

        // Act
        Set<ConstraintViolation<EntradaCompendio>> violations = validator.validate(entrada);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("descripcionDetallada", violations.iterator().next().getPropertyPath().toString());
    }

    private CategoriaCompendio crearCategoria() {
        CategoriaCompendio categoria = new CategoriaCompendio();
        categoria.setNombre(faker.options().option("Bestiario", "Hechizos", "Reglas", "Objetos"));
        categoria.setDescripcion(faker.lorem().sentence());
        return categoria;
    }

    private EntradaCompendio crearEntrada(CategoriaCompendio categoria) {
        EntradaCompendio entrada = new EntradaCompendio();
        entrada.setTitulo(faker.book().title());
        entrada.setDescripcionDetallada(faker.lorem().paragraph());
        entrada.setEstadisticas(faker.lorem().sentence());
        entrada.setCategoria(categoria);
        return entrada;
    }
}

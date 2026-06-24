package com.DragonForge.wiki_service.model;

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

class WikiModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-23: crear articulo con comentario usando datos ficticios")
    void shouldCreateArticleWithCommentUsingFakerData() {
        // Arrange
        Articulo articulo = crearArticulo();
        Comentario comentario = crearComentario(articulo);
        articulo.setComentarios(List.of(comentario));

        // Act
        Set<ConstraintViolation<Articulo>> articuloViolations = validator.validate(articulo);
        Set<ConstraintViolation<Comentario>> comentarioViolations = validator.validate(comentario);

        // Assert
        assertNotNull(articulo);
        assertNotNull(articulo.getFechaCreacion());
        assertTrue(articuloViolations.isEmpty());
        assertTrue(comentarioViolations.isEmpty());
        assertSame(articulo, comentario.getArticulo());
    }

    @Test
    @DisplayName("CP-24: rechazar articulo sin contenido")
    void shouldRejectArticleWithoutContent() {
        // Arrange
        Articulo articulo = crearArticulo();
        articulo.setContenido("");

        // Act
        Set<ConstraintViolation<Articulo>> violations = validator.validate(articulo);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("contenido", violations.iterator().next().getPropertyPath().toString());
    }

    private Articulo crearArticulo() {
        Articulo articulo = new Articulo();
        articulo.setTitulo(faker.book().title());
        articulo.setContenido(faker.lorem().paragraph());
        articulo.setAutor(faker.name().fullName());
        return articulo;
    }

    private Comentario crearComentario(Articulo articulo) {
        Comentario comentario = new Comentario();
        comentario.setTexto(faker.lorem().sentence());
        comentario.setAutorComentario(faker.name().username());
        comentario.setArticulo(articulo);
        return comentario;
    }
}

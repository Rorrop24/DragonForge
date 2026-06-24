package com.DragonForge.asset_service.model;

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

class AssetModelTest {

    private final Faker faker = new Faker();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("CP-15: crear carpeta con archivo usando datos ficticios")
    void shouldCreateFolderWithAssetFileUsingFakerData() {
        // Arrange
        CarpetaActivo carpeta = crearCarpeta();
        ArchivoActivo archivo = crearArchivo(carpeta);
        carpeta.setArchivos(List.of(archivo));

        // Act
        Set<ConstraintViolation<CarpetaActivo>> carpetaViolations = validator.validate(carpeta);
        Set<ConstraintViolation<ArchivoActivo>> archivoViolations = validator.validate(archivo);

        // Assert
        assertNotNull(carpeta);
        assertTrue(carpetaViolations.isEmpty());
        assertTrue(archivoViolations.isEmpty());
        assertSame(carpeta, archivo.getCarpeta());
        assertEquals(1, carpeta.getArchivos().size());
    }

    @Test
    @DisplayName("CP-16: rechazar archivo sin URL")
    void shouldRejectAssetFileWithoutUrl() {
        // Arrange
        ArchivoActivo archivo = crearArchivo(crearCarpeta());
        archivo.setUrlRecurso("");

        // Act
        Set<ConstraintViolation<ArchivoActivo>> violations = validator.validate(archivo);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("urlRecurso", violations.iterator().next().getPropertyPath().toString());
    }

    private CarpetaActivo crearCarpeta() {
        CarpetaActivo carpeta = new CarpetaActivo();
        carpeta.setNombre(faker.lorem().word());
        carpeta.setDescripcion(faker.lorem().sentence());
        return carpeta;
    }

    private ArchivoActivo crearArchivo(CarpetaActivo carpeta) {
        ArchivoActivo archivo = new ArchivoActivo();
        archivo.setNombreArchivo(faker.lorem().word() + ".png");
        archivo.setUrlRecurso(faker.internet().url());
        archivo.setTipoMime(faker.options().option("image/png", "image/jpeg", "audio/mpeg"));
        archivo.setPesoKb(faker.number().randomDouble(2, 1, 2048));
        archivo.setCarpeta(carpeta);
        return archivo;
    }
}

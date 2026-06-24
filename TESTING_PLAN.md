# Plan de Pruebas - DragonForge

## Objetivo

Validar el comportamiento principal de los microservicios de DragonForge mediante pruebas unitarias con JUnit 5 y datos ficticios generados con DataFaker. Las pruebas deben evitar datos reales y seguir la metodologia AAA: Arrange, Act, Assert.

## Herramientas

- Java 21
- Spring Boot
- JUnit 5
- DataFaker 2.4.2
- Maven

## Criterios generales

- Usar DataFaker para crear nombres, correos, textos, cantidades y valores de dominio.
- Mantener cada prueba ordenada con comentarios `Arrange`, `Act` y `Assert`.
- Priorizar pruebas unitarias sin levantar el contexto completo de Spring cuando se validen modelos.
- Usar DataFaker para construir entidades con datos dinamicos y no hardcodeados.
- Cubrir validaciones de entidades, relaciones entre modelos y reglas simples representables sin base de datos.

## Casos de prueba

### CP-01: Registrar usuario

**Modulo:** `user-service`

**Metodo:** creacion y validacion de `Usuario`

**Objetivo:** verificar que un usuario pueda crearse con username, correo y password validos generados por DataFaker.

**Resultado esperado:**

- El objeto `Usuario` existe.
- `fechaRegistro` se inicializa correctamente.
- El correo tiene formato valido.
- No existen violaciones de validacion.

### CP-02: Rechazar correo invalido

**Modulo:** `user-service`

**Metodo:** validacion de `Usuario`

**Objetivo:** comprobar que la anotacion `@Email` detecta correos con formato invalido.

**Resultado esperado:**

- La validacion retorna una violacion.
- La violacion corresponde al campo `email`.

### CP-03: Crear personaje con equipamiento

**Modulo:** `character-service`

**Metodo:** creacion de `Personaje` y `Equipamiento`

**Objetivo:** verificar que un personaje pueda crearse con raza, clase, nivel, puntos de golpe e inventario usando datos ficticios.

**Resultado esperado:**

- El objeto `Personaje` existe.
- El personaje no presenta violaciones de validacion.
- El equipamiento no presenta violaciones de validacion.
- El inventario contiene el item agregado.
- El item mantiene la referencia al personaje.

### CP-04: Tirar dados y guardar historial

**Modulo:** `rng-service`

**Metodo:** `DiceService.roll(int diceSides, int numberOfDice, int modifier)`

**Objetivo:** validar que el servicio genere resultados dentro del rango del dado y solicite guardar la tirada en el repositorio.

**Resultado esperado:**

- La respuesta contiene la cantidad esperada de tiradas individuales.
- Cada tirada esta entre `1` y `diceSides`.
- El total corresponde a la suma de tiradas mas el modificador.
- Se invoca `TiradaRepository.save(...)`.

### CP-05 en adelante: Validaciones por microservicio

**Modulos:** `asset-service`, `compendium-service`, `log-service`, `map-service`, `notification-service`, `wiki-service`, `loot-service`, `rng-service`, `user-service`, `character-service`

**Metodo:** creacion y validacion de entidades con DataFaker.

**Objetivo:** validar que cada modelo principal pueda construirse con datos ficticios y que sus restricciones rechacen valores invalidos.

**Resultado esperado:**

- Las entidades validas no presentan violaciones.
- Las relaciones entre modelos se mantienen correctamente.
- Los campos obligatorios vacios generan violaciones.
- Los rangos invalidos generan violaciones cuando existen restricciones como `@Min` y `@Max`.

## Resumen

| ID | Caso de prueba | Modulo | Tipo |
| --- | --- | --- | --- |
| CP-01 | Registrar usuario con datos validos | `user-service` | Unitaria |
| CP-02 | Rechazar correo invalido | `user-service` | Unitaria |
| CP-03 | Crear personaje con equipamiento | `character-service` | Unitaria |
| CP-04 | Tirar dados y guardar historial | `rng-service` | Unitaria |
| CP-05+ | Validaciones y relaciones por microservicio | Todos los servicios de dominio | Unitaria |

## Comando de ejecucion

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

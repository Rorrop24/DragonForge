# Plan de Pruebas - DragonForge

## Objetivo

Validar el comportamiento principal de los microservicios de DragonForge mediante pruebas unitarias con JUnit 5, Mockito y datos ficticios generados con DataFaker. Las pruebas deben evitar datos reales y seguir la metodologia AAA: Arrange, Act, Assert.

## Herramientas

- Java 21
- Spring Boot
- JUnit 5
- Mockito
- DataFaker 2.4.2
- Maven

## Criterios generales

- Usar DataFaker para crear nombres, correos, textos, cantidades y valores de dominio.
- Mantener cada prueba ordenada con comentarios `Arrange`, `Act` y `Assert`.
- Priorizar pruebas unitarias sin levantar el contexto completo de Spring cuando solo se validen modelos o servicios aislados.
- Usar Mockito para repositorios, clientes HTTP y dependencias externas.
- Cubrir validaciones de entidades, reglas simples de servicios y colaboraciones entre microservicios.

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

### CP-05: Consultar informacion de usuario desde otros servicios

**Modulos:** `wiki-service`, `notification-service`

**Metodo:** clientes WebClient hacia `user-service`

**Objetivo:** validar la colaboracion entre microservicios que consultan datos de usuario.

**Resultado esperado:**

- El cliente recibe un DTO de usuario simulado.
- La respuesta no es nula.
- El servicio consumidor maneja correctamente el usuario retornado.
- Los errores HTTP se prueban con respuestas controladas.

## Resumen

| ID | Caso de prueba | Modulo | Tipo |
| --- | --- | --- | --- |
| CP-01 | Registrar usuario con datos validos | `user-service` | Unitaria |
| CP-02 | Rechazar correo invalido | `user-service` | Unitaria |
| CP-03 | Crear personaje con equipamiento | `character-service` | Unitaria |
| CP-04 | Tirar dados y guardar historial | `rng-service` | Unitaria |
| CP-05 | Consultar informacion de usuario | `wiki-service`, `notification-service` | Integracion aislada |

## Comando de ejecucion

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

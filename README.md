# DragonForge

DragonForge es una plataforma basada en microservicios para apoyar la gestion de partidas de rol. El sistema permite administrar usuarios, personajes, campanas, botin, mapas, wiki, compendio, diarios de aventura, recursos multimedia, notificaciones y tiradas de dados.

El proyecto fue desarrollado con Spring Boot, Spring Cloud Gateway, Eureka, JPA/Hibernate, Flyway, MySQL, Swagger/OpenAPI, JUnit 5 y DataFaker.

## Objetivo del Proyecto

Implementar una arquitectura distribuida con microservicios independientes, comunicacion REST, enrutamiento centralizado mediante API Gateway, persistencia en bases de datos MySQL separadas y documentacion tecnica con Swagger/OpenAPI.

## Arquitectura General

El proyecto sigue el patron Controller-Service-Repository/Model, separando responsabilidades por capas:

- `controller`: expone endpoints REST.
- `service`: contiene la logica de negocio.
- `repository`: gestiona el acceso a datos con Spring Data JPA.
- `model`: define entidades JPA y validaciones.
- `dto`: transporta datos entre servicios cuando corresponde.
- `client`: encapsula comunicacion REST interna con WebClient.

## Microservicios

| Modulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| `eureka-server` | 8761 | Registro y descubrimiento de servicios |
| `gateway` | 9000 | API Gateway centralizado |
| `user-service` | 8084 | Usuarios y campanas |
| `character-service` | 8081 | Personajes e inventario |
| `loot-service` | 8082 | Items y categorias de botin |
| `wiki-service` | 8083 | Articulos y comentarios |
| `log-service` | 8086 | Diarios y entradas de campana |
| `compendium-service` | 8087 | Categorias y entradas de compendio |
| `notification-service` | 8088 | Buzones y notificaciones |
| `asset-service` | 8089 | Carpetas y archivos multimedia |
| `rng-service` | 8096 | Tiradas de dados |
| `map-service` | 8097 | Mapas y ubicaciones |

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Netflix Eureka
- Spring Data JPA
- Hibernate
- MySQL
- Flyway
- Swagger / OpenAPI
- JUnit 5
- DataFaker
- Maven Wrapper

## Requisitos Previos

Antes de ejecutar el proyecto se necesita:

- Java 21 instalado.
- MySQL ejecutandose en `localhost:3306`.
- Usuario MySQL `root` sin password, o ajustar credenciales en cada `application.properties`.
- Puertos disponibles para Eureka, Gateway y microservicios.

## Configuracion de Bases de Datos

Cada microservicio de dominio utiliza una base de datos MySQL independiente. Las URLs JDBC incluyen:

```properties
createDatabaseIfNotExist=true
```

Esto permite crear automaticamente la base de datos si MySQL esta activo y el usuario tiene permisos.

Las migraciones se gestionan con Flyway en:

```text
src/main/resources/db/migration
```

Formato de migraciones:

```text
V1__create_table_*.sql
V2__inserts_*.sql
```

Configuracion aplicada:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.jpa.hibernate.ddl-auto=validate
```

## Ejecucion Local

Orden recomendado:

1. Iniciar MySQL.
2. Levantar `eureka-server`.
3. Levantar los microservicios de dominio.
4. Levantar `gateway`.
5. Consumir la API desde el Gateway en `http://localhost:9000`.

Comando para ejecutar un modulo en Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Ejemplo:

```powershell
cd eureka-server
.\mvnw.cmd spring-boot:run
```

## API Gateway

El Gateway se ejecuta en:

```text
http://localhost:9000
```

La configuracion de rutas esta en:

```text
gateway/src/main/resources/application.yaml
```

Ejemplos de rutas centralizadas:

```text
http://localhost:9000/api/v1/users
http://localhost:9000/api/v1/personajes
http://localhost:9000/api/v1/loot
http://localhost:9000/api/dice
```

## Comunicacion REST Interna

Algunos microservicios se comunican entre si usando `WebClient`:

- `character-service` consume `rng-service`.
- `wiki-service` consume `user-service`.
- `notification-service` consume `user-service`.

Las URLs se externalizan en `application.properties`:

```properties
services.rng.url=http://localhost:8096
services.user.url=http://localhost:8084
```

## Swagger / OpenAPI

Los controladores estan documentados con:

- `@Tag`
- `@Operation`
- `@ApiResponse`
- `@ApiResponses`

Swagger UI esta disponible en cada servicio levantado:

```text
http://localhost:{PUERTO}/swagger-ui/index.html
```

Ejemplos:

```text
http://localhost:8084/swagger-ui/index.html
http://localhost:8081/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html
http://localhost:8096/swagger-ui/index.html
```

## Pruebas

Las pruebas unitarias usan JUnit 5 y DataFaker para generar datos ficticios. Se validan entidades, relaciones entre modelos y restricciones como `@NotBlank`, `@NotNull`, `@Min`, `@Max` y `@Email`.

Ejecutar pruebas de un modulo:

```powershell
.\mvnw.cmd test
```

Servicios con pruebas DataFaker:

- `user-service`
- `character-service`
- `loot-service`
- `rng-service`
- `map-service`
- `wiki-service`
- `notification-service`
- `asset-service`
- `compendium-service`
- `log-service`

## Estructura del Repositorio

```text
DragonForge/
|-- asset-service/
|-- character-service/
|-- compendium-service/
|-- eureka-server/
|-- gateway/
|-- log-service/
|-- loot-service/
|-- map-service/
|-- notification-service/
|-- rng-service/
|-- user-service/
|-- wiki-service/
|-- pom.xml
|-- README.md
|-- TESTING_PLAN.md
```

## Evidencias para la Defensa

Para la entrega tecnica se recomienda mostrar:

- Proyecto en GitHub.
- Historial de commits tecnicos.
- Tablero Trello o similar con tareas asignadas.
- Eureka funcionando en `http://localhost:8761`.
- Gateway funcionando en `http://localhost:9000`.
- Swagger UI de servicios principales.
- Bases de datos creadas automaticamente por Flyway.
- Pruebas con DataFaker.

## Nota sobre Docker

Docker no se incluye en esta version debido a problemas del entorno de evaluacion. La ejecucion local queda documentada mediante Maven, MySQL, Eureka y API Gateway.

## Autor / Equipo

Proyecto academico desarrollado para evaluacion de arquitectura de microservicios.

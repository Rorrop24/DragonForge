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

Para ejecutar con Docker se necesita:

- Docker Desktop instalado y ejecutandose.
- PowerShell o terminal de Windows.
- Puertos disponibles: `3306`, `8761`, `9000`, `8081`, `8082`, `8083`, `8084`, `8086`, `8087`, `8088`, `8089`, `8096`, `8097`.

Para ejecutar sin Docker se necesita:

- Java 21 instalado.
- MySQL ejecutandose en `localhost:3306`.
- Usuario MySQL `root` sin password, o ajustar credenciales en cada `application.properties`.

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
V3__seed_*.sql
```

Configuracion aplicada:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.jpa.hibernate.ddl-auto=validate
```

En Docker, MySQL se levanta como contenedor y cada servicio se conecta usando el hostname interno `mysql`.

Para revisar que Flyway ejecuto las migraciones:

```powershell
docker exec dragonforge-mysql mysql -uroot -e "SELECT * FROM dnd_loot_db.flyway_schema_history;"
```

Para revisar datos cargados automaticamente:

```powershell
docker exec dragonforge-mysql mysql -uroot -e "SELECT * FROM dnd_loot_db.items;"
docker exec dragonforge-mysql mysql -uroot -e "SELECT * FROM dnd_wiki_db.articulos;"
```

## Ejecucion con Docker Compose

La forma recomendada de ejecutar el proyecto es usando Docker Compose. El proyecto incluye:

- `Dockerfile`: imagen base para compilar y ejecutar cualquier microservicio.
- `docker-compose.yml`: orquesta MySQL, Eureka, Gateway y todos los microservicios.
- `.dockerignore`: evita copiar archivos innecesarios al contexto de build.

### Levantar el proyecto

Desde la carpeta raiz del proyecto:

```powershell
cd C:\Users\rorro\Desktop\DragonForge
docker compose build
docker compose up -d
docker compose ps
```

Si todo esta correcto, los contenedores deben aparecer como `Up` y MySQL como `healthy`.

### Apagar el proyecto

```powershell
docker compose down
```

### Reiniciar desde cero

Si se necesita borrar la base Docker local y hacer que Flyway vuelva a crear tablas y datos:

```powershell
docker compose down -v
docker compose build
docker compose up -d
```

Importante: `docker compose down -v` borra el volumen local de MySQL.

### Ver logs

```powershell
docker compose logs -f gateway
docker compose logs -f eureka-server
docker compose logs -f mysql
```

## Como funciona el Dockerfile

El `Dockerfile` usa una construccion multi-stage:

```dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
ARG MODULE
WORKDIR /workspace
COPY . .
RUN mvn -pl ${MODULE} -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
ARG MODULE
COPY --from=build /workspace/${MODULE}/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

La primera etapa compila el modulo Maven indicado por `MODULE`.
La segunda etapa ejecuta solo el `.jar` generado usando Java 21 JRE.

Esto permite usar un solo Dockerfile para todos los microservicios. En `docker-compose.yml`, cada servicio indica el modulo que debe compilar:

```yaml
build:
  context: .
  args:
    MODULE: user-service
```

## Como funciona docker-compose.yml

`docker-compose.yml` levanta todo el ecosistema:

- `mysql`: base de datos MySQL 8.4.
- `eureka-server`: registro y descubrimiento de servicios.
- `gateway`: entrada central por `http://localhost:9000`.
- Microservicios de dominio: usuarios, personajes, loot, wiki, mapas, logs, assets, notificaciones, compendio y dados.

Dentro de Docker no se usa `localhost` para comunicacion entre contenedores. Se usan nombres de servicios:

| Local tradicional | Docker Compose |
| --- | --- |
| `localhost:3306` | `mysql:3306` |
| `localhost:8761` | `eureka-server:8761` |
| `localhost:8084` | `user-service:8084` |
| `localhost:8096` | `rng-service:8096` |

Por eso en Compose las variables de entorno reemplazan las URLs internas:

```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/dnd_user_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
```

## Ejecucion Local sin Docker

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
http://localhost:9000/api/v1/loot/items
http://localhost:9000/api/v1/wiki/articulos
http://localhost:9000/api/v1/maps
http://localhost:9000/api/dice/historial
```

Rutas utiles para probar desde el navegador:

| Servicio | URL por Gateway |
| --- | --- |
| Usuarios | `http://localhost:9000/api/v1/users` |
| Personajes | `http://localhost:9000/api/v1/personajes` |
| Loot | `http://localhost:9000/api/v1/loot/items` |
| Wiki | `http://localhost:9000/api/v1/wiki/articulos` |
| Mapas | `http://localhost:9000/api/v1/maps` |
| Dados | `http://localhost:9000/api/dice/historial` |
| Assets | `http://localhost:9000/api/v1/assets/carpetas` |
| Compendio | `http://localhost:9000/api/v1/compendium/categorias` |
| Logs | `http://localhost:9000/api/v1/logs/diarios` |
| Notificaciones | `http://localhost:9000/api/v1/notifications/buzones` |

## Eureka

Eureka se abre en:

```text
http://localhost:8761
```

En Eureka se ven los microservicios registrados como `UP`.

Nota importante: los links verdes de Eureka pueden apuntar a nombres internos de Docker como:

```text
asset-service:8089
wiki-service:8083
user-service:8084
```

Esos nombres funcionan entre contenedores, pero normalmente no abren desde el navegador de Windows. Para probar desde el navegador se debe usar `localhost` o el Gateway:

```text
http://localhost:9000/api/v1/wiki/articulos
http://localhost:9000/api/v1/loot/items
http://localhost:9000/api/v1/users
```

Si aparece `Whitelabel Error Page` al entrar a una raiz como `http://localhost:8083`, significa que el servicio esta vivo pero no existe una ruta `/`. Se debe entrar a un endpoint real, por ejemplo:

```text
http://localhost:8083/api/v1/wiki/articulos
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
- `Dockerfile` explicando el build multi-stage.
- `docker-compose.yml` explicando contenedores, puertos, variables de entorno, red interna y volumen MySQL.
- Comando `docker compose ps` con contenedores `Up`.
- Eureka funcionando en `http://localhost:8761`.
- Gateway funcionando en `http://localhost:9000`.
- Endpoints funcionando desde el Gateway.
- Swagger UI de servicios principales.
- Bases de datos creadas automaticamente por Flyway.
- Pruebas con DataFaker.

## Autor / Equipo

Proyecto academico desarrollado para evaluacion de arquitectura de microservicios.

Markdown
# 🐉 DragonForge - Arquitectura de Microservicios

> Plataforma basada en microservicios diseñada para apoyar la gestión de partidas de rol (RPG). El sistema permite administrar de manera distribuida usuarios, personajes, campañas, botín, mapas, wiki, compendio, diarios de aventura, recursos multimedia, notificaciones y tiradas de dados.

---

## 🎯 Objetivo del Proyecto
Implementar una arquitectura distribuida con microservicios independientes, comunicación REST, enrutamiento centralizado mediante **API Gateway**, persistencia en bases de datos MySQL separadas y documentación técnica interactiva con **Swagger/OpenAPI**.

---

## 👥 El gran Equipo de Desarrollo
* Rodrigo Catalan (Ror)
* Jose Quezada (XxCamPasteroXx)

---

## 🏗️ Arquitectura General
El proyecto sigue el patrón **CSR (Controller-Service-Repository/Model)**, separando estrictamente las responsabilidades por capas:

* 🟢 **`controller`**: Expone los endpoints REST para la comunicación externa.
* 🟡 **`service`**: Contiene y procesa toda la lógica de negocio.
* 🔵 **`repository`**: Gestiona el acceso a datos utilizando Spring Data JPA.
* 🟣 **`model`**: Define las entidades JPA y sus respectivas validaciones.
* 🟠 **`dto`**: Transporta datos de forma segura entre servicios.
* 🟤 **`client`**: Encapsula la comunicación REST interna utilizando WebClient.

---

## 🧩 Módulos y Microservicios

| Módulo | Puerto | Responsabilidad Principal |
| :--- | :---: | :--- |
| 🌐 **`eureka-server`** | `8761` | Registro y descubrimiento dinámico de servicios. |
| 🚦 **`gateway`** | `9000` | API Gateway centralizado y ruteo de peticiones. |
| 👤 **`user-service`** | `8084` | Gestión de cuentas de usuarios y campañas. |
| ⚔️ **`character-service`** | `8081` | Administración de hojas de personajes e inventario. |
| 💰 **`loot-service`** | `8082` | Catálogo de ítems y categorías de botín. |
| 📖 **`wiki-service`** | `8083` | Artículos de lore, facciones y comentarios. |
| 📜 **`log-service`** | `8086` | Diarios de aventura y registro de eventos. |
| 📚 **`compendium-service`** | `8087` | Bestiario, reglas y entradas de compendio. |
| 🔔 **`notification-service`**| `8088` | Sistema de buzones y alertas. |
| 🖼️ **`asset-service`** | `8089` | Gestión de carpetas y archivos multimedia (Tokens/Imágenes).|
| 🎲 **`rng-service`** | `8096` | Motor matemático para tiradas de dados. |
| 🗺️ **`map-service`** | `8097` | Administración de mapas tácticos y ubicaciones. |

---

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java 21
* **Framework Core:** Spring Boot
* **Arquitectura Cloud:** Spring Cloud Gateway, Netflix Eureka
* **Persistencia:** Spring Data JPA, Hibernate, MySQL, Flyway
* **Documentación:** Swagger / OpenAPI
* **Testing:** JUnit 5, DataFaker
* **Gestión:** Maven Wrapper

---

## ⚙️ Requisitos Previos
Antes de ejecutar el proyecto, asegúrate de contar con:
1. **Java 21** instalado en tu sistema.
2. **MySQL Server** ejecutándose en `localhost:3306`.
3. Usuario MySQL `root` sin contraseña (o ajustar las credenciales en cada archivo `application.properties`).
4. Puertos listados en la tabla superior disponibles y sin bloqueos de firewall.

---

## 🗄️ Configuración de Bases de Datos
Cada microservicio de dominio utiliza una base de datos MySQL 100% independiente. Las URLs JDBC incluyen el parámetro:
```properties
createDatabaseIfNotExist=true
Nota: Esto permite crear automáticamente la base de datos si MySQL está activo y el usuario tiene los permisos adecuados.

Las migraciones estructurales y poblado de datos se gestionan con Flyway en la ruta src/main/resources/db/migration, utilizando el formato estándar:

V1__create_table_*.sql

V2__inserts_*.sql

Configuración aplicada en application.properties:

Properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.jpa.hibernate.ddl-auto=validate
🚀 Ejecución Local
Orden de encendido recomendado:

Iniciar servicio de MySQL (Ej: XAMPP).

Levantar eureka-server y esperar a que inicialice.

Levantar los microservicios de dominio (Character, User, etc.).

Levantar el gateway al final.

Consumir la API a través del puerto central: http://localhost:9000.

Comando de ejecución (Windows / PowerShell):

PowerShell
# Ejemplo levantando Eureka Server
cd eureka-server
.\mvnw.cmd spring-boot:run
🚦 API Gateway y Rutas
El Gateway centraliza todo el tráfico en http://localhost:9000. La configuración de rutas se encuentra definida en gateway/src/main/resources/application.yaml.

Ejemplos de endpoints centralizados:

👤 Usuarios: http://localhost:9000/api/v1/users

⚔️ Personajes: http://localhost:9000/api/v1/personajes

💰 Botín: http://localhost:9000/api/v1/loot

🎲 Dados: http://localhost:9000/api/dice

🔄 Comunicación REST Interna
El ecosistema utiliza WebClient para la comunicación síncrona entre microservicios específicos:

character-service ➔ Consume a rng-service.

wiki-service ➔ Consume a user-service.

notification-service ➔ Consume a user-service.

Las URLs están externalizadas para fácil configuración en application.properties:

Properties
services.rng.url=http://localhost:8096
services.user.url=http://localhost:8084
📚 Swagger / OpenAPI
Todos los controladores están documentados formalmente utilizando las anotaciones @Tag, @Operation, @ApiResponse y @ApiResponses.

La interfaz gráfica interactiva (Swagger UI) está disponible en cada servicio levantado bajo la ruta:
👉 http://localhost:{PUERTO}/swagger-ui/index.html

(Ejemplos: Puertos 8084, 8081, 8082, 8096)

🧪 Pruebas Unitarias (Testing)
El proyecto asegura la calidad del código mediante pruebas unitarias desarrolladas con JUnit 5 y DataFaker (para la generación de entornos de prueba con datos ficticios realistas).

Se validan entidades, relaciones de modelos y restricciones de integridad (@NotBlank, @NotNull, @Min, @Max, @Email).

Comando para ejecutar pruebas por módulo:

PowerShell
.\mvnw.cmd test
Los 10 microservicios de dominio cuentan con cobertura de pruebas utilizando DataFaker.

📂 Estructura del Repositorio
Plaintext
DragonForge/
├── asset-service/
├── character-service/
├── compendium-service/
├── eureka-server/
├── gateway/
├── log-service/
├── loot-service/
├── map-service/
├── notification-service/
├── rng-service/
├── user-service/
├── wiki-service/
├── pom.xml
├── README.md
└── TESTING_PLAN.md
🛡️ Evidencias para la Defensa Técnica
Para la evaluación y revisión del proyecto se dispone de:

✅ Repositorio versionado en GitHub con historial de commits técnicos.

✅ Tablero de planificación (Trello/GitHub Projects) con tareas asignadas.

✅ Eureka Server registrando instancias (http://localhost:8761).

✅ API Gateway enrutando tráfico correctamente (http://localhost:9000).

✅ Documentación interactiva Swagger UI operativa.

✅ Bases de datos autogeneradas vía Flyway.

✅ Pruebas unitarias funcionales con DataFaker.

🐳 Nota sobre Docker: El despliegue en contenedores Docker no se incluye en esta versión específica del repositorio debido a restricciones del entorno de evaluación. La ejecución local queda documentada mediante Maven, MySQL, Eureka y API Gateway.

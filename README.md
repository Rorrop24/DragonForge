# 🐉 DragonForge - Arquitectura de Microservicios

## 📖 Contexto y Dominio del Proyecto
**DragonForge** es una plataforma integral diseñada para la gestión de partidas de rol (específicamente Dungeons & Dragons). El sistema permite administrar de manera distribuida a los jugadores (Usuarios), sus Héroes (Personajes), el inventario (Loot), la construcción del mundo (Mapas y Wiki), y la simulación matemática de eventos (RNG/Dados), entre otros. Toda la arquitectura está construida bajo un ecosistema de microservicios independientes, comunicados a través de un API Gateway y registrados dinámicamente mediante Eureka Server.

## 👥 El gran Equipo de Desarrollo
* Rodrigo Catalan (Ror)
* [Jose Quezada (XxCamPasteroXx)]

---

## 🛠️ Listado de Microservicios Implementados
El ecosistema consta de un **Servidor de Descubrimiento (Eureka)**, un **Enrutador (API Gateway)** y **10 Microservicios** con responsabilidades separadas (Patrón CSR):

1. **Eureka Server** (Puerto `8761`): Registro y descubrimiento de servicios.
2. **API Gateway** (Puerto `9000`): Enrutador central y balanceador de carga.
3. **Character Service** (Puerto `8081`): Gestión de hojas de personajes y estadísticas.
4. **User Service** (Puerto `8084`): Gestión de cuentas de jugadores, DMs y Campañas.
5. **Asset Service** (Puerto `8089`): Manejo de imágenes, tokens y recursos gráficos.
6. **Map Service** (Puerto `8097`): Administración de mapas tácticos de batalla.
7. **Compendium Service** (Puerto `8087`): Catálogo de bestiario, hechizos y reglas.
8. **Log Service** (Puerto `8086`): Diarios de aventura y registro de eventos.
9. **Loot Service** (Puerto `8082`): Gestión de botín, tesoros e inventario.
10. **Notification Service** (Puerto `8088`): Sistema de mensajería y alertas.
11. **RNG Service** (Puerto `8096`): Motor matemático de tiradas de dados (d4, d20, etc.).
12. **Wiki Service** (Puerto `8083`): Enciclopedia de lore, facciones y ciudades.

---

## 🚦 Rutas Principales del API Gateway
Todo el tráfico externo debe apuntar al Gateway (`http://localhost:9000`). El Gateway redirigirá automáticamente a los microservicios

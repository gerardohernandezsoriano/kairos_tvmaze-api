# tvmaze-api

API Spring Boot que consume [TVMaze](https://www.tvmaze.com/api) para buscar shows y mostrar sus detalles, con comentarios de usuario persistidos en MongoDB Atlas y una cache TTL para las respuestas.

## Stack

- **Java 21** · **Spring Boot 3.x**
- **Arquitectura hexagonal** (puertos y adaptadores)
- **Spring MVC** (`spring-boot-starter-webmvc`) + **RestClient** para el cliente externo
- **Spring Data MongoDB** con Atlas, `MongoTemplate` en el adaptador de salida
- **Spring Actuator** (`/actuator/health`, `/actuator/info`)
- **Maven Wrapper** (`./mvnw`)
- **Bean Validation** (`spring-boot-starter-validation`)

## Requisitos

- JDK 21
- Una base MongoDB Atlas (o local) con autenticación
- Acceso saliente a `https://api.tvmaze.com` (origen de datos)
- (Opcional) Maven 3.9+ si prefieres `mvn` en vez de `./mvnw`

## Configuración (variables de entorno)

Copia el archivo `.env.example` a `.env` y completa los valores:

| Variable | Descripción | Requerida |
| --- | --- | --- |
| `MONGODB_URI` | Connection string de MongoDB (p. ej. Atlas `mongodb+srv://...`) | Sí |
| `TVMAZE_BASE_URL` | Base URL de TVMaze (default `https://api.tvmaze.com`) | No |
| `TVMAZE_CACHE_TTL_SECONDS` | TTL de la cache de shows en segundos (default `3600`) | No |

> `.env` está en `.gitignore`: **no commitees credenciales**.

Carga las variables y arranca la aplicación:

```bash
set -a && source .env && set +a
./mvnw spring-boot:run
```

## Uso

La aplicación escucha en `http://localhost:8080`.

### Buscar shows por título

```bash
curl "http://localhost:8080/shows/search?q=batman"
```

Respuesta (200): lista de shows con sus comentarios.

### Obtener un show por ID

```bash
curl "http://localhost:8080/shows/975"
```

- `200`: show con comentarios
- `404`: show no encontrado
- `503`: servicio TVMaze no disponible

### Crear un comentario

```bash
curl -X POST "http://localhost:8080/comments" \
  -H "Content-Type: application/json" \
  -d '{"showId":975,"comment":"Genial","rating":5}'
```

El `comment` no puede estar vacío y el `rating` debe estar entre `1` y `5`; si no, se responde `400`.

## Arquitectura

Estructura hexagonal por capas:

```
src/main/java/com/kairos/tvmaze/tvmaze_api/
├── adapter/in/        # Controladores + DTOs + manejo global de errores
├── adapter/out/       # Cliente TVMaze (RestClient) + adaptador MongoDB (cache/comentarios)
├── aplication/        # Servicios de aplicación y modelos de resultado
├── domain/            # Modelo de dominio, puertos (in/out), excepciones de negocio
└── TVMazeConfig.java  # Configuración del RestClient (timeouts 3s/5s)
```

### Flujo principal

`Controller (in) → Puerto de entrada → Caso de uso (aplication) → Puerto de salida → Adaptador (out)`:

- **Búsqueda / detalle**: `SearchController` → `SearchShowsService` / `GetShowService` → `RestClient` hacia TVMaze; la cache MongoDB con TTL evita repetir llamadas.
- **Comentarios**: `CommentController` → `CreateCommentService` → `MongoCommentAdapter`.

Los 404 de TVMaze se traducen a `ShowNotFoundException`, y los errores de red/timeout a `ExternalServiceException` (503), ambos manejados por `GlobalExceptionHandler` con respuestas de error consistentes (`ErrorResponse`).

## Tests

```bash
set -a && source .env && set +a
./mvnw test
```

Cubre los puertos de entrada/salida con tests slice de Spring (test para cada adaptador/servicio) además del test de contexto de arranque. `MONGODB_URI` debe estar definida para el arranque del contexto (puedes apuntarla a una instancia local de Mongo para los tests).

## Proyecto

Repositorio del ejercicio de evaluación técnica — arquitectura hexagonal, cache TTL y manejo de errores con Spring Boot.

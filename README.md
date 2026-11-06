# Heightwander Server

Backend API de Heightwander (Kotlin / Ktor / Koin / Mongo). Repo/codename:
`peakhub_server`. Esqueleto inspirado en `trailhub_server`: arranque
`Main` → `Program` → `KtorServer`, sin módulos de dominio aún.

## Requisitos

- JDK 17+
- MongoDB alcanzable (`DATABASE_URI` / `DATABASE_NAME`)

## Arranque local

```bash
cp .env.example .env   # ajusta URI si hace falta
export $(grep -v '^#' .env | xargs)
./gradlew run
# curl http://127.0.0.1:5801/health
```

## Build

```bash
./gradlew shadowJar
# artefacto: build/libs/HeightwanderServer.jar
```

## Layout

```text
src/main/kotlin/
  Main.kt
  net/asere/heightwander/server/
    main/           # Program, DI, KtorServer, health
    shared/core/    # Environment, Mongo, errors, Logger
```

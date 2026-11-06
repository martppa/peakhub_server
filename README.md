# Heightwander Server

Backend API de Heightwander (Kotlin / Ktor / Koin / Mongo). Repo/codename:
`peakhub_server`. Inspirado en `trailhub_server`: arranque
`Main` → `Program` → `KtorServer`. Auth API portado de Trazavia (sin HTML web).

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

## Auth (API)

Requiere `JWT_TOKEN_KEY` (≥32 bytes) y `GOOGLE_CLIENT_ID`.

| Método | Path |
|--------|------|
| POST | `/api/user/login/google` |
| POST | `/api/user/login` |
| POST | `/api/user/register` |
| POST | `/api/user/refresh` |
| POST | `/api/user/verify` / `/api/user/verify/resend` |
| POST | `/api/user/password/code` / `recover` / `change` |
| PATCH/DELETE | `/api/user` (Bearer JWT) |

Profile/email/storage/trail/subscription van con stubs no-op por ahora.

## Layout

```text
src/main/kotlin/
  Main.kt
  net/asere/heightwander/server/
    main/           # Program, DI, KtorServer, health
    account/        # Auth API (Google, JWT, email)
    profile/        # NoOp ProfileService (+ stubs)
    shared/         # core, account, profile contracts
```

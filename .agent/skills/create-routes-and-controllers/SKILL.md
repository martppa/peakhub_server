---
name: create-routes-and-controllers
description: >-
  Creates Ktor API routes and controllers in peakhub_server following the
  feature-module layout. Use when adding endpoints, Controllers, Routes, or
  registering routes in KtorServer.
---

# Creating Ktor Routes and Controllers (peakhub_server)

## 1. Controller

- **Location**: `src/main/kotlin/net/asere/peakhub/server/<module>/presentation/api/<Name>Controller.kt`
- Inject use cases via constructor.
- Each public method maps to one endpoint.
- Map domain → API models with `asApi()` before returning.

```kotlin
class PeakController(
    private val getPeakById: GetPeakById,
) {
    suspend fun getById(id: String): PeakApi = getPeakById(id).asApi()
}
```

When Omni Ktor is introduced (same pattern as trailhub_server), prefer
`AnyResponseContainerHost` + `responseIntent { }` with module exception mappers.

## 2. Routes

- **Location**: `…/<module>/presentation/api/<Name>Routes.kt`
- Extension on `KtorServer`: `fun KtorServer.xxxRoutes(application: Application)`
- Inject controller with Koin; define paths inside `routing { }`

```kotlin
fun KtorServer.peakRoutes(application: Application): Unit = with(application) {
    val controller: PeakController by inject()
    routing {
        get("/api/peaks/{id}") {
            call.respond(controller.getById(call.parameters["id"].orEmpty()))
        }
    }
}
```

## 3. Register

Call `xxxRoutes(this)` from `KtorServer.start` and register the controller in the
feature `di/*Module.kt`; add that module in `Program.initializeDi()`.

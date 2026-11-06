---
name: exposing-a-service
description: >-
  Exposes a peakhub_server feature module to other modules via shared Service
  interface + Proxy. Use when adding XxxService, XxxProxy, or shared models.
---

# Exposing a Module (Service)

## 1. Service interface in `shared`

`net.asere.heightwander.server.shared.<module>.domain.XxxService`

```kotlin
interface PeakService {
    suspend fun getPeak(id: String): PeakShared
}
```

## 2. Shared models

Lean DTOs under `shared/<module>/domain/model/` with `Shared` suffix
(e.g. `PeakShared`).

## 3. Proxy in the owning module

`net.asere.heightwander.server.<module>.domain.XxxProxy` implements the service,
delegates to use cases, maps domain ↔ shared. No business logic in the proxy.

## 4. DI

Bind `factoryOf(::XxxProxy) bind XxxService::class` in the feature Koin module.

---
name: enum-mapping
description: >-
  Maps enums between Domain, API, and BSON layers with exhaustive when.
  Use when adding or editing enum fields across layers in peakhub_server.
---

# Enum Mapping

1. Each layer defines its own `enum class` (do not map enums to bare `String`).
2. Map with exhaustive `when` extension functions (`asApi()`, `asDomain()`, `asBson()`).
3. For lists: `.map { it.asTarget() }` / `?.map { … } ?: emptyList()`.

```kotlin
fun Difficulty.Level.asApi(): DifficultyApi.Level = when (this) {
    Difficulty.Level.LEVEL_1 -> DifficultyApi.Level.LEVEL_1
    // …
}
```

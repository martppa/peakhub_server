---
name: one-type-per-file
description: >-
  Enforces one top-level Kotlin type per source file in peakhub_server. Use when
  creating or editing .kt files, DTOs, models, use cases, BSON types, or API models.
---

# One type per file (Kotlin)

## Hard rule

Each `.kt` file may declare **at most one** top-level `class`, `data class`,
`enum class`, `interface`, `object`, or `sealed` type.

File name **must match** that type: `FooBar.kt` → `class FooBar`.

## Allowed in the same file

- Nested types (`Input`, `Result`, sealed subclasses nested in the parent)
- Top-level extension functions / `typealias` / constants that support that
  single type, or a dedicated `*Mappers.kt` / `*Support.kt` file with **no**
  top-level type

## Not allowed

Multiple top-level types in one file (`Foo` + `Bar`, bag-of-use-cases files).

## When editing

If you touch a file that already has multiple top-level types, **split it**
before adding more.

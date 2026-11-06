---
name: bson-enums
description: >-
  Defines how peakhub_server maps domain enums to MongoDB BSON enums. Use when
  creating or editing *Bson models, adding enum fields to documents, or mapping
  domain enums to/from the database.
---

# BSON Enums (peakhub_server)

MongoDB’s POJO codec serializes Kotlin enums as strings using the constant name.
No manual `.name` / `valueOf()` is needed on BSON fields.

## Hard rules

1. **BSON enum fields are Kotlin enums**, never `String?`.
2. **Domain enums stay in domain**; BSON enums live only in the data layer.
3. **Map explicitly with `when`** — never `Enum.valueOf(string)` in `asDomain()`.
4. **Do not leak BSON enums** outside `data/datasource/database/`.

## Preferred layout

Standalone enum file under `<module>/data/datasource/database/bson/`:
`<Concept>Bson` with `asBson()` / `asDomain()` extensions.

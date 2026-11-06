package net.asere.heightwander.server.shared.core.domain.tools

import java.util.UUID

fun generateId() = UUID.randomUUID().toString().replace("-", "")

fun isValidUUID(uuid: String): Boolean = try {
    UUID.fromString(uuid)
    true
} catch (_: IllegalArgumentException) {
    false
}

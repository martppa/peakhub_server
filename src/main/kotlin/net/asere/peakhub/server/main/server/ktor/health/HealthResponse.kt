package net.asere.peakhub.server.main.server.ktor.health

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String = "ok",
)

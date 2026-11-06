package net.asere.peakhub.server.shared.core.data.environment

import net.asere.peakhub.server.shared.core.domain.environment.Environment

class EnvironmentData : Environment {
    override fun getMongoDatabaseUri(): String =
        System.getenv("DATABASE_URI")
            ?: error("DATABASE_URI environment variable is required")

    override fun getMongoDatabaseName(): String =
        System.getenv("DATABASE_NAME") ?: "peakhub"

    override fun getServerPort(): Int =
        System.getenv("PORT")?.toIntOrNull() ?: 5801

    override fun isDebug(): Boolean =
        System.getenv("DEBUG")?.equals("true", ignoreCase = true) == true
}

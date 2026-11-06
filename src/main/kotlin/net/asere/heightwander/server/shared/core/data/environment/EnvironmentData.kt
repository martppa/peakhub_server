package net.asere.heightwander.server.shared.core.data.environment

import net.asere.heightwander.server.shared.core.domain.environment.Environment

class EnvironmentData : Environment {
    override fun getMongoDatabaseUri(): String =
        System.getenv("DATABASE_URI")
            ?: error("DATABASE_URI environment variable is required")

    override fun getMongoDatabaseName(): String =
        System.getenv("DATABASE_NAME") ?: "peakhub_server"

    override fun getServerPort(): Int =
        System.getenv("PORT")?.toIntOrNull() ?: 5801

    override fun isDebug(): Boolean =
        System.getenv("DEBUG")?.equals("true", ignoreCase = true) == true

    override fun getJwtTokenKey(): String {
        val key =
            System.getenv("JWT_TOKEN_KEY")
                ?: error("JWT_TOKEN_KEY environment variable is required")
        require(key.toByteArray(Charsets.UTF_8).size >= 32) {
            "JWT_TOKEN_KEY must be at least 32 bytes"
        }
        return key
    }

    override fun getGoogleClientId(): String =
        System.getenv("GOOGLE_CLIENT_ID")
            ?: error("GOOGLE_CLIENT_ID environment variable is required")

    override fun getAppleClientId(): String =
        System.getenv("APPLE_CLIENT_ID") ?: "net.asere.heightwander.ios"

    override fun isPublicAccessEnabled(): Boolean =
        System.getenv("PUBLIC_ACCESS_ENABLED")?.equals("true", ignoreCase = true) != false

    override fun getWebHost(): String =
        System.getenv("WEB_HOST") ?: "https://heightwander.com"
}

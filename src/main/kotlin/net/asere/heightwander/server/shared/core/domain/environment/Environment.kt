package net.asere.heightwander.server.shared.core.domain.environment

interface Environment {
    fun getMongoDatabaseUri(): String

    fun getMongoDatabaseName(): String

    fun getServerPort(): Int

    fun isDebug(): Boolean
}

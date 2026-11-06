package net.asere.peakhub.server.shared.core.data.database

class DatabaseComposable(
    private val databasesList: List<Database>,
) : Database {
    override suspend fun connect() {
        databasesList.forEach { it.connect() }
    }
}

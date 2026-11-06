package net.asere.peakhub.server.shared.core.data.database

interface Database {
    suspend fun connect()
}

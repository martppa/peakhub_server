package net.asere.heightwander.server.shared.core.data.database

interface Database {
    suspend fun connect()
}

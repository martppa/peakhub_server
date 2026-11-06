package net.asere.heightwander.server.shared.storage.domain

interface StorageService {
    suspend fun deleteMediasByUserId(userId: String)
}

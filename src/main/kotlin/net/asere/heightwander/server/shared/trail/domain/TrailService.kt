package net.asere.heightwander.server.shared.trail.domain

interface TrailService {
    suspend fun deleteUserTrails(userId: String)
}

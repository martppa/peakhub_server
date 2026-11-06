package net.asere.peakhub.server.main

import kotlinx.coroutines.runBlocking
import net.asere.peakhub.server.main.di.mainModule
import net.asere.peakhub.server.main.server.Server
import net.asere.peakhub.server.shared.core.data.database.Database
import net.asere.peakhub.server.shared.core.domain.environment.Environment
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class Program : KoinComponent {
    private val environment: Environment by inject()
    private val server: Server by inject()
    private val database: Database by inject()

    private fun initializeDi() {
        startKoin {
            modules(mainModule)
        }
    }

    private suspend fun initDatabase() {
        database.connect()
    }

    fun run() =
        runBlocking {
            initializeDi()
            initDatabase()
            server.start(environment.getServerPort())
        }
}

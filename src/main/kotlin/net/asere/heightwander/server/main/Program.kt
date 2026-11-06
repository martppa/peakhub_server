package net.asere.heightwander.server.main

import kotlinx.coroutines.runBlocking
import net.asere.heightwander.server.main.di.mainModule
import net.asere.heightwander.server.main.server.Server
import net.asere.heightwander.server.shared.core.data.database.Database
import net.asere.heightwander.server.shared.core.domain.environment.Environment
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

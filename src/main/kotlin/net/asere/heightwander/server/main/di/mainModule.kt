package net.asere.heightwander.server.main.di

import kotlinx.coroutines.CoroutineScope
import net.asere.heightwander.server.main.error.LogExceptionHandler
import net.asere.heightwander.server.main.log.ConsoleLogger
import net.asere.heightwander.server.main.server.Server
import net.asere.heightwander.server.main.server.ktor.KtorServer
import net.asere.heightwander.server.shared.core.data.database.Database
import net.asere.heightwander.server.shared.core.data.database.DatabaseComposable
import net.asere.heightwander.server.shared.core.data.database.mongodb.MongodbDatabase
import net.asere.heightwander.server.shared.core.data.environment.EnvironmentData
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.tools.Logger
import net.asere.heightwander.server.shared.core.presentation.api.error.ExceptionHandler
import net.asere.heightwander.server.shared.core.presentation.api.error.ExceptionHandlerComposite
import net.asere.heightwander.server.shared.core.presentation.api.execution.GlobalCoroutineScope
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule =
    module {
        single<Environment> { EnvironmentData() }
        single { ConsoleLogger(get()) } bind Logger::class
        single {
            ExceptionHandlerComposite(
                listOf(
                    LogExceptionHandler(get()),
                ),
            )
        } bind ExceptionHandler::class
        factoryOf(::GlobalCoroutineScope) bind CoroutineScope::class
        single { MongodbDatabase(get(), get(), get()) }
        single {
            DatabaseComposable(
                listOf(
                    get<MongodbDatabase>(),
                ),
            )
        } bind Database::class
        single<Server> { KtorServer() }
    }

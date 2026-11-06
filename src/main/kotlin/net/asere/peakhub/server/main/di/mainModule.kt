package net.asere.peakhub.server.main.di

import kotlinx.coroutines.CoroutineScope
import net.asere.peakhub.server.main.error.LogExceptionHandler
import net.asere.peakhub.server.main.log.ConsoleLogger
import net.asere.peakhub.server.main.server.Server
import net.asere.peakhub.server.main.server.ktor.KtorServer
import net.asere.peakhub.server.shared.core.data.database.Database
import net.asere.peakhub.server.shared.core.data.database.DatabaseComposable
import net.asere.peakhub.server.shared.core.data.database.mongodb.MongodbDatabase
import net.asere.peakhub.server.shared.core.data.environment.EnvironmentData
import net.asere.peakhub.server.shared.core.domain.environment.Environment
import net.asere.peakhub.server.shared.core.domain.tools.Logger
import net.asere.peakhub.server.shared.core.presentation.api.error.ExceptionHandler
import net.asere.peakhub.server.shared.core.presentation.api.error.ExceptionHandlerComposite
import net.asere.peakhub.server.shared.core.presentation.api.execution.GlobalCoroutineScope
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

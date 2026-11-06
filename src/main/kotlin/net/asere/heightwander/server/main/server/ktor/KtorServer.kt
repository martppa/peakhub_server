package net.asere.heightwander.server.main.server.ktor

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import net.asere.heightwander.server.main.server.Server
import net.asere.heightwander.server.main.server.ktor.health.healthRoutes
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.model.error.BadRequestError
import net.asere.heightwander.server.shared.core.domain.model.error.ForbiddenError
import net.asere.heightwander.server.shared.core.domain.model.error.NotFoundError
import net.asere.heightwander.server.shared.core.domain.model.error.UnauthorizedError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ForbiddenApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.NotFoundApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.UnauthorizedApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.UnknownApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.toApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KtorServer :
    Server,
    KoinComponent {
    private val appEnvironment: Environment by inject()

    override fun start(port: Int) {
        embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                json()
            }
            install(StatusPages) {
                exception<BadRequestError> { call, cause ->
                    call.respond(HttpStatusCode.BadRequest, cause.toApi())
                }
                exception<NotFoundError> { call, _ ->
                    call.respond(HttpStatusCode.NotFound, NotFoundApiError())
                }
                exception<ForbiddenError> { call, _ ->
                    call.respond(HttpStatusCode.Forbidden, ForbiddenApiError())
                }
                exception<UnauthorizedError> { call, _ ->
                    call.respond(HttpStatusCode.Unauthorized, UnauthorizedApiError())
                }
                exception<Throwable> { call, _ ->
                    call.respond(HttpStatusCode.InternalServerError, UnknownApiError())
                }
            }
            install(CORS) {
                if (appEnvironment.isDebug()) {
                    anyHost()
                }
                allowHeaders { true }
                allowNonSimpleContentTypes = true
                allowMethod(HttpMethod.Options)
                allowMethod(HttpMethod.Get)
                allowMethod(HttpMethod.Head)
                allowMethod(HttpMethod.Post)
                allowMethod(HttpMethod.Put)
                allowMethod(HttpMethod.Patch)
                allowMethod(HttpMethod.Delete)
            }
            install(Compression)
            healthRoutes(this)
        }.start(wait = true)
    }
}

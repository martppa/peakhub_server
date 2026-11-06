package net.asere.peakhub.server.main.server.ktor.health

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import net.asere.peakhub.server.main.server.ktor.KtorServer

fun KtorServer.healthRoutes(application: Application): Unit =
    with(application) {
        routing {
            get("/health") {
                call.respond(HealthResponse())
            }
        }
    }

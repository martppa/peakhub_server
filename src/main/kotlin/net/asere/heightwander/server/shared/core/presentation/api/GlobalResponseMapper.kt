package net.asere.heightwander.server.shared.core.presentation.api

import net.asere.omni.ktor.Response
import net.asere.omni.ktor.ResponseMapper

class GlobalResponseMapper : ResponseMapper<Any, Any> {
    override fun valueOf(input: Any): Response<Any> = Response(body = input)
}

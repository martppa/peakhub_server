package net.asere.heightwander.server.shared.core.presentation.api

import net.asere.omni.ktor.Response
import net.asere.omni.ktor.ResponseMapper

class NoResponseMapper<Input, Output> : ResponseMapper<Input, Output> {
    override fun valueOf(input: Input): Response<Output> = throw IllegalStateException("No response implementation")
}

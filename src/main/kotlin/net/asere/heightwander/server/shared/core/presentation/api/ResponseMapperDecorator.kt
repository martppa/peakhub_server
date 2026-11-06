package net.asere.heightwander.server.shared.core.presentation.api

import net.asere.omni.ktor.Response
import net.asere.omni.ktor.ResponseMapper

open class ResponseMapperDecorator<Input, Output> : ResponseMapper<Input, Output> {
    internal var mapper: ResponseMapper<Input, Output> = NoResponseMapper()

    override fun valueOf(input: Input): Response<Output> = mapper.valueOf(input)

    operator fun plus(rightHand: ResponseMapper<Input, Output>): ResponseMapperDecorator<Input, Output> {
        if (rightHand is ResponseMapperDecorator) {
            rightHand.mapper = this
            return rightHand
        }
        mapper = rightHand
        return this
    }
}

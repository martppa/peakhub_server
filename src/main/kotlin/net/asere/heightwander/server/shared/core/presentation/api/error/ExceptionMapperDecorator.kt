package net.asere.heightwander.server.shared.core.presentation.api.error

import net.asere.omni.ktor.ExceptionResponseMapper
import net.asere.omni.ktor.Response

open class ExceptionMapperDecorator : ExceptionResponseMapper<Any> {
    internal var mapper: ExceptionResponseMapper<Any> = GlobalExceptionMapper()

    override fun valueOf(input: Throwable): Response<Any> = mapper.valueOf(input)

    operator fun plus(rightHand: ExceptionResponseMapper<Any>): ExceptionMapperDecorator {
        if (rightHand is ExceptionMapperDecorator) {
            rightHand.mapper = this
            return rightHand
        }
        mapper = rightHand
        return this
    }
}

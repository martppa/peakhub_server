package net.asere.heightwander.server.shared.core.presentation.api.error

import net.asere.heightwander.server.shared.core.domain.model.error.BadRequestError
import net.asere.heightwander.server.shared.core.domain.model.error.ForbiddenError
import net.asere.heightwander.server.shared.core.domain.model.error.NotFoundError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.ForbiddenApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.NotFoundApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.UnknownApiError
import net.asere.heightwander.server.shared.core.presentation.api.model.error.toApi
import net.asere.heightwander.server.shared.core.presentation.api.model.error.toResponse
import net.asere.omni.ktor.ExceptionResponseMapper
import net.asere.omni.ktor.Response

class GlobalExceptionMapper : ExceptionResponseMapper<Any> {
    override fun valueOf(input: Throwable): Response<Any> =
        when (input) {
            is BadRequestError -> input.toApi().toResponse()
            NotFoundError -> NotFoundApiError().toResponse()
            ForbiddenError -> ForbiddenApiError().toResponse()
            else -> UnknownApiError().toResponse()
        }
}

package net.asere.heightwander.server.shared.core.domain.model.error

open class BadRequestError(
    override val message: String,
) : GenericError()

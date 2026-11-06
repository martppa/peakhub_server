package net.asere.peakhub.server.main.error

import net.asere.peakhub.server.shared.core.domain.environment.Environment
import net.asere.peakhub.server.shared.core.presentation.api.error.ExceptionHandler

class LogExceptionHandler(
    private val environment: Environment,
) : ExceptionHandler {
    override fun handle(throwable: Throwable) {
        if (environment.isDebug()) {
            System.err.println("###########################################################")
            System.err.println("## ERROR: ${throwable::class.simpleName}")
            System.err.println("###########################################################")
            throwable.printStackTrace()
        }
    }
}

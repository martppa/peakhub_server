package net.asere.heightwander.server.shared.core.presentation.api.error

import kotlinx.coroutines.CoroutineExceptionHandler

class ExceptionHandlerComposite(
    private val handlers: List<ExceptionHandler>,
) : ExceptionHandler {
    override fun handle(throwable: Throwable) {
        handlers.forEach { it.handle(throwable) }
    }
}

fun ExceptionHandler.asCoroutineHandler() =
    CoroutineExceptionHandler { _, throwable ->
        handle(throwable)
    }

package net.asere.heightwander.server.shared.core.presentation.api.error

interface ExceptionHandler {
    fun handle(throwable: Throwable)
}

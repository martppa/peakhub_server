package net.asere.peakhub.server.shared.core.presentation.api.error

interface ExceptionHandler {
    fun handle(throwable: Throwable)
}

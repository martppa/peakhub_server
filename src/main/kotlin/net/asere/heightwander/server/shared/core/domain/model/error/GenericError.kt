package net.asere.heightwander.server.shared.core.domain.model.error

abstract class GenericError : Throwable()

val Throwable.requiredMessage get() = message ?: "unknown"

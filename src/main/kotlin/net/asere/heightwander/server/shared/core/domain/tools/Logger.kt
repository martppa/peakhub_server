package net.asere.heightwander.server.shared.core.domain.tools

interface Logger {
    fun debug(message: String)

    fun info(message: String)

    fun error(message: String)
}

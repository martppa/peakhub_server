package net.asere.heightwander.server.main.log

import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.domain.tools.Logger

class ConsoleLogger(
    private val environment: Environment,
) : Logger {
    override fun debug(message: String) {
        if (environment.isDebug()) {
            println(message)
        }
    }

    override fun info(message: String) {
        println(message)
    }

    override fun error(message: String) {
        System.err.println(message)
    }
}

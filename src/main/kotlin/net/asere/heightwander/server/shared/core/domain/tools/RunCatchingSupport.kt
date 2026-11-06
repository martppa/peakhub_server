@file:Suppress("UNCHECKED_CAST")

package net.asere.heightwander.server.shared.core.domain.tools

fun <T> catch(block: () -> T): RunResult = try {
    ValueResult(value = block())
} catch (exception: Throwable) {
    ErrorRunResult(exception)
}

infix fun <T> RunResult.throws(error: Throwable): T = when (this) {
    is ValueResult<*> -> this.value as T
    is ErrorRunResult -> throw error
}

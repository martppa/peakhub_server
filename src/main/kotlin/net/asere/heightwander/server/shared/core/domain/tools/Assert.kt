package net.asere.heightwander.server.shared.core.domain.tools

data class AssertResult(
    internal val value: Boolean,
)

fun assert(block: () -> Boolean): AssertResult = AssertResult(block())

infix fun AssertResult.or(error: Throwable) {
    if (!value) {
        throw error
    }
}

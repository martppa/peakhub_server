@file:Suppress("UNCHECKED_CAST")

package net.asere.heightwander.server.shared.core.domain.tools

data class ValueResult<T>(
    val value: T,
) : RunResult()

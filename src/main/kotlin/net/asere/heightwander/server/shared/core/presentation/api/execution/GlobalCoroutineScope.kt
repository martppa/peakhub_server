package net.asere.heightwander.server.shared.core.presentation.api.execution

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.asere.heightwander.server.shared.core.presentation.api.error.ExceptionHandler
import net.asere.heightwander.server.shared.core.presentation.api.error.asCoroutineHandler

class GlobalCoroutineScope(
    exceptionHandler: ExceptionHandler,
) : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        SupervisorJob() + exceptionHandler.asCoroutineHandler()
}

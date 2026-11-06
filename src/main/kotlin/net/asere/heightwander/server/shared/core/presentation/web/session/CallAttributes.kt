package net.asere.heightwander.server.shared.core.presentation.web.session

import io.ktor.util.AttributeKey
import net.asere.heightwander.server.shared.core.presentation.common.UserContext

object CallAttributes {
    val userContext = AttributeKey<UserContext>("userContext")
}

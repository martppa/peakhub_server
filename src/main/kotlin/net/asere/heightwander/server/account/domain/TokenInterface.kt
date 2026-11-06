package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.Token
import net.asere.heightwander.server.account.domain.model.User

interface TokenInterface {
    fun createAccess(user: User): String

    fun createRefresh(user: User): String

    fun verify(token: String): Token
}

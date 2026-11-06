package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.mappable.Session

interface SessionRepository {
    fun createSession(user: User): Session
}

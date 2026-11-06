package net.asere.heightwander.server.account.data

import net.asere.heightwander.server.account.domain.SessionRepository
import net.asere.heightwander.server.account.domain.TokenInterface
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.account.domain.model.mappable.Session

class SessionDataRepository(
    private val tokenInterface: TokenInterface,
) : SessionRepository {
    override fun createSession(user: User): Session {
        val token = tokenInterface.createAccess(user)
        val refresh = tokenInterface.createRefresh(user)
        return Session(
            userId = user.id,
            token = token,
            refresh = refresh,
        )
    }
}

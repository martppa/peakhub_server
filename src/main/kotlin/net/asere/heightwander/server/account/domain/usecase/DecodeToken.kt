package net.asere.heightwander.server.account.domain.usecase

import net.asere.heightwander.server.account.domain.TokenInterface
import net.asere.heightwander.server.account.domain.model.Token

class DecodeToken(
    private val tokenInterface: TokenInterface,
) {
    operator fun invoke(token: String): Token = tokenInterface.verify(token)
}

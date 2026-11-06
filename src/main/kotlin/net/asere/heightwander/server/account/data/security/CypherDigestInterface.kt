package net.asere.heightwander.server.account.data.security

import net.asere.heightwander.server.account.domain.model.CypherInterface

class CypherDigestInterface : CypherInterface {
    private val authentication = PasswordAuthentication()

    override fun hash(input: String) = authentication.hash(input.toCharArray())

    override fun authenticate(
        password: String,
        hash: String,
    ) = authentication.authenticate(password.toCharArray(), hash)

    override fun needsRehash(hash: String) = authentication.needsRehash(hash)
}

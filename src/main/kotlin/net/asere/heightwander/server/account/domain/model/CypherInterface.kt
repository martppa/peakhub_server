package net.asere.heightwander.server.account.domain.model

interface CypherInterface {
    fun hash(input: String): String

    fun authenticate(
        password: String,
        hash: String,
    ): Boolean

    fun needsRehash(hash: String): Boolean = false
}

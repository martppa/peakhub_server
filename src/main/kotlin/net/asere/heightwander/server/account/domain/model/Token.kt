package net.asere.heightwander.server.account.domain.model

data class Token(
    val userId: String,
    val userType: User.Type,
    val tokenType: Type,
    val tokenVersion: Int = 0,
    val expires: Long,
    val value: String,
) : AccountAggregate {
    enum class Type {
        Access,
        Refresh,
    }
}

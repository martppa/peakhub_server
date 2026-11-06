package net.asere.heightwander.server.shared.account.domain.model

data class UserShared(
    val id: String,
    val name: String,
    val email: String,
    val type: Type,
    val source: Source,
    val emailVerified: Boolean = false,
    val createdAt: Long? = null,
) {
    enum class Type {
        Customer,
        Moderator,
        Creator,
        Admin,
        Tester,
    }

    enum class Source {
        Email,
        Google,
        Apple,
    }
}

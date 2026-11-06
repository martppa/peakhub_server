package net.asere.heightwander.server.shared.account.domain

sealed interface DeleteUserResult {
    data object Success : DeleteUserResult

    data object NotFound : DeleteUserResult
}

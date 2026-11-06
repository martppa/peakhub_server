package net.asere.heightwander.server.shared.account.domain

sealed interface UpdateUserResult {
    data object Success : UpdateUserResult

    data object NotFound : UpdateUserResult

    data object AlreadyExists : UpdateUserResult

    data object InvalidUsername : UpdateUserResult

    data object InvalidEmail : UpdateUserResult

    data object InvalidPassword : UpdateUserResult
}

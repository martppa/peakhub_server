package net.asere.heightwander.server.shared.profile.domain

sealed interface UpdateProfileResult {
    data object Success : UpdateProfileResult

    data object NotFound : UpdateProfileResult

    data object NameTooLarge : UpdateProfileResult
}

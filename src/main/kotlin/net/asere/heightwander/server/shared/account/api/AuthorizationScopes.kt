package net.asere.heightwander.server.shared.account.api

interface AuthorizationScopes {
    companion object {
        const val API_LOGGED_SCOPE = "api_logged"
        const val WEB_LOGGED_SCOPE = "web_logged"
        const val MODERATOR_SCOPE = "moderator"
        const val ADMIN_SCOPE = "admin"
    }
}

package net.asere.heightwander.server.account.domain.extension

fun String?.isValidEmail(): Boolean {
    val regexPattern = Regex("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
    return this?.let { regexPattern.matches(it) } ?: false
}

fun String?.isValidUsername(): Boolean {
    val regexPattern = Regex("^[\\w\\-]{3,20}\$")
    return this?.let { regexPattern.matches(it) } ?: false
}

fun String?.isValidPassword(): Boolean {
    val regexPattern = Regex("^.{12,64}$")
    return this?.let { regexPattern.matches(it) } ?: false
}

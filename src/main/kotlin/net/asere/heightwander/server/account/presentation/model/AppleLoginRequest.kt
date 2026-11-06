package net.asere.heightwander.server.account.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppleLoginRequest(
    @SerialName("idToken") val idToken: String,
    @SerialName("acceptedTerms") val acceptedTerms: Boolean = false,
)

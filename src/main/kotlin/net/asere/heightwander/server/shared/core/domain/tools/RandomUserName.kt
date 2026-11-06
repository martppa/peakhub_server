package net.asere.heightwander.server.shared.core.domain.tools

import java.text.Normalizer
import java.util.UUID
import kotlin.random.Random

fun generateRandomUserName(): String = "User_${UUID.randomUUID().toString().replace("-", "")}"

fun String.formatGoogleUserName(): String {
    val withoutSpaces = replace(Regex("\\s"), "")
    val lowercased = withoutSpaces.lowercase()
    val normalized =
        Normalizer
            .normalize(lowercased, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .take(10)
    val randomNumber = Random.nextInt(10000, 100000)
    return "${normalized}_$randomNumber"
}

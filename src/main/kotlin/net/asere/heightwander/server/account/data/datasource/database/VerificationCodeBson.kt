package net.asere.heightwander.server.account.data.datasource.database

import java.util.*
import net.asere.heightwander.server.account.domain.model.mappable.VerificationCode
import org.bson.codecs.pojo.annotations.BsonId

data class VerificationCodeBson(
    @param:BsonId
    var id: String? = null,
    var userId: String? = null,
    var value: String? = null,
    var createdAt: Date = Date(),
)

fun VerificationCode.toBson() = VerificationCodeBson(
    id = id,
    userId = userId,
    value = value,
    createdAt = createdAt,
)

fun VerificationCodeBson.asDomain() = VerificationCode(
    id = id!!,
    userId = userId!!,
    value = value!!,
    createdAt = createdAt,
)

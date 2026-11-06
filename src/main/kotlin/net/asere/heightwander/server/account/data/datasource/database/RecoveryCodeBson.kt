package net.asere.heightwander.server.account.data.datasource.database

import java.util.*
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import org.bson.codecs.pojo.annotations.BsonId

data class RecoveryCodeBson(
    @param:BsonId
    var id: String? = null,
    var userId: String? = null,
    var value: String? = null,
    var createdAt: Date = Date(),
)

fun RecoveryCode.toBson() = RecoveryCodeBson(
    id = id,
    userId = userId,
    value = value,
    createdAt = createdAt,
)

fun RecoveryCodeBson.asDomain() = RecoveryCode(
    id = id!!,
    userId = userId!!,
    value = value!!,
    createdAt = createdAt,
)

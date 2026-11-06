package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode

interface RecoveryRepository {
    suspend fun saveCode(code: RecoveryCode)

    suspend fun removeCode(userId: String)

    suspend fun getCode(value: String): RecoveryCode?
}

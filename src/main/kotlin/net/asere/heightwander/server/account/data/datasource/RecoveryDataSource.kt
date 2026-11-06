package net.asere.heightwander.server.account.data.datasource

import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode

interface RecoveryDataSource {
    suspend fun saveCode(recoveryCode: RecoveryCode)

    suspend fun removeCode(userId: String)

    suspend fun getCode(value: String): RecoveryCode?
}

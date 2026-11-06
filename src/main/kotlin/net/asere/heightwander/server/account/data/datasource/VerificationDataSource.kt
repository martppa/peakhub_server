package net.asere.heightwander.server.account.data.datasource

import net.asere.heightwander.server.account.domain.model.mappable.VerificationCode

interface VerificationDataSource {
    suspend fun saveCode(code: VerificationCode)

    suspend fun removeCode(userId: String)

    suspend fun getCode(value: String): VerificationCode?
}

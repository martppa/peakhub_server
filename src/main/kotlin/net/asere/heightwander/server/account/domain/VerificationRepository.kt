package net.asere.heightwander.server.account.domain

import net.asere.heightwander.server.account.domain.model.mappable.VerificationCode

interface VerificationRepository {
    suspend fun saveCode(code: VerificationCode)

    suspend fun removeCode(userId: String)

    suspend fun getCode(value: String): VerificationCode?
}

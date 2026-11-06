package net.asere.heightwander.server.account.data

import net.asere.heightwander.server.account.data.datasource.VerificationDataSource
import net.asere.heightwander.server.account.domain.VerificationRepository
import net.asere.heightwander.server.account.domain.model.mappable.VerificationCode
import net.asere.heightwander.server.shared.core.domain.tools.OneTimeCode

class VerificationDataRepository(
    private val dataSource: VerificationDataSource,
) : VerificationRepository {
    override suspend fun saveCode(code: VerificationCode) = dataSource.saveCode(code.copy(value = OneTimeCode.digest(code.value)))

    override suspend fun removeCode(userId: String) = dataSource.removeCode(userId)

    override suspend fun getCode(value: String): VerificationCode? = dataSource.getCode(OneTimeCode.digest(value))
}

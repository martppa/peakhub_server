package net.asere.heightwander.server.account.data

import net.asere.heightwander.server.account.data.datasource.RecoveryDataSource
import net.asere.heightwander.server.account.domain.RecoveryRepository
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import net.asere.heightwander.server.shared.core.domain.tools.OneTimeCode

class RecoveryDataRepository(
    private val dataSource: RecoveryDataSource,
) : RecoveryRepository {
    override suspend fun saveCode(code: RecoveryCode) {
        dataSource.saveCode(code.copy(value = OneTimeCode.digest(code.value)))
    }

    override suspend fun removeCode(userId: String) {
        dataSource.removeCode(userId)
    }

    override suspend fun getCode(value: String): RecoveryCode? = dataSource.getCode(OneTimeCode.digest(value))
}

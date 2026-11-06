package net.asere.heightwander.server.account.data.datasource.database

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import net.asere.heightwander.server.account.data.datasource.RecoveryDataSource
import net.asere.heightwander.server.account.domain.model.mappable.RecoveryCode
import net.asere.heightwander.server.shared.core.data.database.mongodb.MongodbDatabase

class RecoveryMongodbDataSource(
    database: MongodbDatabase,
) : RecoveryDataSource {
    companion object {
        private const val COLLECTION_NAME = "recovery_codes"
    }

    private val collection: MongoCollection<RecoveryCodeBson> =
        database.collection(COLLECTION_NAME, RecoveryCodeBson::class.java)

    override suspend fun saveCode(recoveryCode: RecoveryCode) {
        val filter = Filters.eq("userId", recoveryCode.userId)
        collection.deleteMany(filter)
        collection.insertOne(recoveryCode.toBson())
    }

    override suspend fun removeCode(userId: String) {
        val filter = Filters.eq("userId", userId)
        collection.deleteOne(filter)
    }

    override suspend fun getCode(value: String): RecoveryCode? {
        val filter = Filters.eq("value", value)
        return collection
            .find(filter)
            .toList()
            .firstOrNull()
            ?.asDomain()
    }
}

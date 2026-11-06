package net.asere.heightwander.server.account.data.datasource.database

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import net.asere.heightwander.server.account.data.datasource.VerificationDataSource
import net.asere.heightwander.server.account.domain.model.mappable.VerificationCode
import net.asere.heightwander.server.shared.core.data.database.mongodb.MongodbDatabase

class VerificationMongodbDataSource(
    database: MongodbDatabase,
) : VerificationDataSource {
    companion object {
        private const val COLLECTION_NAME = "verification_codes"
    }

    private val collection: MongoCollection<VerificationCodeBson> =
        database.collection(COLLECTION_NAME, VerificationCodeBson::class.java)

    override suspend fun saveCode(code: VerificationCode) {
        val filter = Filters.eq("userId", code.userId)
        collection.deleteMany(filter)
        collection.insertOne(code.toBson())
    }

    override suspend fun removeCode(userId: String) {
        val filter = Filters.eq("userId", userId)
        collection.deleteOne(filter)
    }

    override suspend fun getCode(value: String): VerificationCode? {
        val filter = Filters.eq("value", value)
        return collection
            .find(filter)
            .toList()
            .firstOrNull()
            ?.asDomain()
    }
}

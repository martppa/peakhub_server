package net.asere.heightwander.server.account.data.datasource.database

import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import java.util.*
import java.util.regex.Pattern
import net.asere.heightwander.server.account.data.datasource.UserDataSource
import net.asere.heightwander.server.account.domain.model.User
import net.asere.heightwander.server.shared.core.data.database.mongodb.MongodbDatabase

class UserMongodbDataSource(
    database: MongodbDatabase,
) : UserDataSource {
    companion object {
        private const val COLLECTION_NAME = "users"
    }

    private val collection: MongoCollection<UserBson> =
        database.collection(COLLECTION_NAME, UserBson::class.java)

    override suspend fun save(user: User): User {
        val bson = user.asBson().copy(createdAt = Date())
        collection.insertOne(bson)
        return bson.asDomain()
    }

    override suspend fun getByName(name: String): User? {
        val query = BasicDBObject()
        query["name"] = Pattern.compile("^" + name + "$", Pattern.CASE_INSENSITIVE)
        return collection.find(query).first()?.asDomain()
    }

    override suspend fun getById(id: String): User? {
        val filter = Filters.eq("_id", id)
        return collection.find(filter).first()?.asDomain()
    }

    override suspend fun getByIds(ids: List<String>): List<User> {
        val filter = Filters.`in`("_id", ids)
        return collection.find(filter).toList().map { it.asDomain() }
    }

    override suspend fun getByEmail(email: String): User? {
        val query = BasicDBObject()
        query["email"] = Pattern.compile("^" + email + "$", Pattern.CASE_INSENSITIVE)
        return collection.find(query).first()?.asDomain()
    }

    override suspend fun getByExternalId(externalId: String): User? {
        val filter = Filters.eq("externalId", externalId)
        return collection.find(filter).first()?.asDomain()
    }

    override suspend fun listUsers(): List<User> = collection.find().toList().map { it.asDomain() }

    override suspend fun searchUsers(query: String): List<User> {
        val escaped = Pattern.quote(query)
        val filter =
            Filters.or(
                Filters.regex("name", ".*$escaped.*", "i"),
                Filters.regex("email", ".*$escaped.*", "i"),
            )
        return collection.find(filter).toList().map { it.asDomain() }
    }

    override suspend fun countUsers(): Long = collection.countDocuments()

    override suspend fun deleteUser(id: String) {
        val filter = Filters.eq("_id", id)
        collection.deleteOne(filter)
    }

    override suspend fun updateUser(user: User) {
        val filters = Filters.eq("_id", user.id)
        val updates = BasicDBObject()
        updates["\$set"] = user.asBson()
        collection.updateOne(filters, updates)
    }
}

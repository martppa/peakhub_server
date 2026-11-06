package net.asere.peakhub.server.shared.core.data.database.mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.AggregateIterable
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.asere.peakhub.server.shared.core.data.database.Database
import net.asere.peakhub.server.shared.core.domain.environment.Environment
import net.asere.peakhub.server.shared.core.domain.tools.Logger
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.conversions.Bson

class MongodbDatabase(
    private val logger: Logger,
    private val environment: Environment,
    private val coroutineScope: CoroutineScope,
) : Database {
    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase

    private fun buildSettings(uri: String): MongoClientSettings {
        val pojoCodecRegistry =
            fromProviders(
                PojoCodecProvider
                    .builder()
                    .automatic(true)
                    .build(),
            )
        val codecRegistry =
            fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry,
            )
        return MongoClientSettings
            .builder()
            .applyConnectionString(ConnectionString(uri))
            .codecRegistry(codecRegistry)
            .build()
    }

    override suspend fun connect() {
        client = MongoClients.create(buildSettings(environment.getMongoDatabaseUri()))
        database = client.getDatabase(environment.getMongoDatabaseName())
        logger.info("Connected to MongoDB database '${environment.getMongoDatabaseName()}'")
        coroutineScope.launch { createIndexes() }
    }

    private fun createIndexes() {
        // Indexes are registered per feature module as they are added.
        logger.info("No feature indexes registered yet.")
    }

    fun <T> collection(
        name: String,
        clazz: Class<T>,
    ): MongoCollection<T> = database.getCollection(name, clazz)

    fun <T : Bson> aggregate(pipeline: List<T>): AggregateIterable<Document> =
        database.aggregate(pipeline)
}
